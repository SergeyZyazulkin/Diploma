package zsp.diploma.mintriang.algorithm.step.impl;

import zsp.diploma.mintriang.algorithm.step.BaseTriangulationStep;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.*;
import zsp.diploma.mintriang.util.Geometry;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BaseTriangulation implements BaseTriangulationStep {

    @Override
    public Triangulation buildBaseTriangulation(GeometryFactory geometryFactory, List<Point> points)
            throws TriangulationException {

        ConvexHull convexHull = new GrahamScan().buildConvexHull(geometryFactory, points);
        List<Point> boundaryPoints = convexHull.getBoundaryPoints();
        Set<Point> adding = new HashSet<>(points);
        List<Triangle> triangles = triangulateConvexHull(boundaryPoints, adding);
        triangulateOtherPoints(triangles, adding);
        return buildTriangulation(geometryFactory, points, triangles);
    }

    private List<Triangle> triangulateConvexHull(List<Point> boundaryPoints, Set<Point> adding) {
        int boundarySize = boundaryPoints.size();
        int first = 2;
        int last = boundarySize - 2;

        while (Geometry.calcTurn(
                boundaryPoints.get(0), boundaryPoints.get(1), boundaryPoints.get(first)) == Geometry.Turn.COLLINEAR) {

            ++first;
        }

        while (Geometry.calcTurn(boundaryPoints.get(0),
                boundaryPoints.get(boundarySize - 1), boundaryPoints.get(last)) == Geometry.Turn.COLLINEAR) {

            --last;
        }

        IntStream.rangeClosed(first - 1, last + 1)
                .boxed()
                .map(boundaryPoints::get)
                .forEach(adding::remove);

        adding.remove(boundaryPoints.get(0));

        return IntStream.rangeClosed(first, last + 1)
                .parallel()
                .boxed()
                .map(i -> new Triangle(boundaryPoints.get(0), boundaryPoints.get(i - 1), boundaryPoints.get(i)))
                .collect(Collectors.toList());
    }

    private void triangulateOtherPoints(List<Triangle> triangles, Set<Point> adding) {
        for (Point p : adding) {
            List<Triangle> newTriangles = new ArrayList<>();
            Iterator<Triangle> triangleIterator = triangles.iterator();

            while (triangleIterator.hasNext()) {
                Triangle triangle = triangleIterator.next();
                List<Triangle> splitted = triangle.split(p);

                if (splitted != null) {
                    newTriangles.addAll(splitted);
                    triangleIterator.remove();
                }
            }

            triangles.addAll(newTriangles);
        }
    }

    private Triangulation buildTriangulation(
            GeometryFactory geometryFactory, List<Point> points, List<Triangle> triangles) {

        List<Edge> edges = triangles.parallelStream()
                .flatMap(t -> t.getEdges(geometryFactory).stream())
                .distinct()
                .collect(Collectors.toList());

        return geometryFactory.createTriangulation(points, edges).normalize();
    }

    private static class Triangle {

        private Point p1;
        private Point p2;
        private Point p3;
        private Geometry.Turn turn;

        public Triangle(Point p1, Point p2, Point p3) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
            this.turn = Geometry.calcTurn(p1, p2, p3);
        }

        private List<Triangle> splitEdge(Point e1, Point e2, Point p, Point n) {
            return Arrays.asList(new Triangle(e1, p, n), new Triangle(e2, p, n));
        }

        private List<Triangle> splitTriangle(Point n) {
            return Arrays.asList(new Triangle(p1, p2, n), new Triangle(p2, p3, n), new Triangle(p1, p3, n));
        }

        public List<Triangle> split(Point p) {
            if (Geometry.isInside(p1, p2, p)) {
                return splitEdge(p1, p2, p3, p);
            } else if (Geometry.isInside(p2, p3, p)) {
                return splitEdge(p2, p3, p1, p);
            } else if (Geometry.isInside(p1, p3, p)) {
                return splitEdge(p1, p3, p2, p);
            } else if (Geometry.calcTurn(p1, p2, p) == turn && Geometry.calcTurn(p2, p3, p) == turn &&
                    Geometry.calcTurn(p3, p1, p) == turn) {

                return splitTriangle(p);
            } else {
                return null;
            }
        }

        public List<Edge> getEdges(GeometryFactory geometryFactory) {
            return Arrays.asList(geometryFactory.createEdge(p1, p2, false),
                    geometryFactory.createEdge(p2, p3, false),
                    geometryFactory.createEdge(p3, p1, false));
        }
    }
}
