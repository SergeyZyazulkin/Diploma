package zsp.diploma.mintriang.algorithm.step.impl;

import zsp.diploma.mintriang.algorithm.step.GeneralPolygonsStep;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.base.DisjointDataSet;
import zsp.diploma.mintriang.model.geometry.*;
import zsp.diploma.mintriang.model.geometry.impl.EdgeImpl;
import zsp.diploma.mintriang.util.Geometry;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GeneralPolygonsKruskal implements GeneralPolygonsStep {

    @Override
    public List<GeneralPolygon> buildGeneralPolygons(GeometryFactory geometryFactory, ConvexHull convexHull)
            throws TriangulationException {

        GeneralPolygon generalPolygon = geometryFactory.createGeneralPolygon(convexHull);
        List<Point> points = generalPolygon.getPoints(false);
        List<Edge> edges = buildAllEdges(geometryFactory, points);
        edges = edges.parallelStream().sorted(Comparator.comparingDouble(Edge::getLength)).collect(Collectors.toList());
        DisjointDataSet disjointDataSet = new DisjointDataSet(points.size());
        Geometry.kruskal(generalPolygon, disjointDataSet, edges);
        generalPolygon.mergeEdges();
        return split(geometryFactory, generalPolygon);
    }

    private List<Edge> buildAllEdges(GeometryFactory geometryFactory, List<Point> points) {
        int pointsCount = points.size();
        List<Edge> edges = new ArrayList<>();
        IntStream.range(0, pointsCount).parallel().forEach(i -> points.get(i).setIndex(i));

        for (int i = 0; i < pointsCount; ++i) {
            for (int j = i + 1; j < pointsCount; ++j) {
                edges.add(geometryFactory.createEdge(points.get(i), points.get(j), false));
            }
        }

        return edges;
    }

    private List<GeneralPolygon> split(GeometryFactory geometryFactory, GeneralPolygon generalPolygon) {
        List<Point> allPoints = generalPolygon.getPoints(true);
        List<GeneralPolygon> generalPolygons = new ArrayList<>();
        int boundaryPointsCount = generalPolygon.getBoundaryPoints().size();

        for (int i = 0; i < boundaryPointsCount; ++i) {
            Point currentPoint = allPoints.get(i);

            if (!currentPoint.isVisited()) {
                int next = i < boundaryPointsCount - 1 ? i + 1 : 0;
                generalPolygons.add(findGeneralPolygon(geometryFactory, currentPoint, allPoints.get(next)));
            }
        }

        return generalPolygons;
    }

    private GeneralPolygon findGeneralPolygon(GeometryFactory geometryFactory, Point current, Point previous) {
        List<Point> boundaryPoints = new ArrayList<>();
        boundaryPoints.add(current);
        boundaryPoints.add(previous);

        Point temp;
        Edge firstEdge = new EdgeImpl(previous, current);
        Edge currentEdge;
        current.setVisited(current.getVisited() - 1);
        Set<Point> points = new HashSet<>();
        Set<Edge> edges = new HashSet<>();

        do {
            current.visit();
            points.add(current);
            edges.add(geometryFactory.createEdge(current, previous, true));
            temp = current;
            current = current.getNext(previous, false);
            previous = temp;
            currentEdge = new EdgeImpl(previous, current);
        } while (!firstEdge.equals(currentEdge));

        Point first = boundaryPoints.get(0);
        previous = first;
        current = boundaryPoints.get(1);
        boundaryPoints.remove(1);
        Point next;

        while (current != first) {
            boundaryPoints.add(current);
            next = previous;

            do {
                next = current.getNext(next, false);
            } while (!points.contains(next) || !edges.contains(geometryFactory.createEdge(current, next, false)));

            previous = current;
            current = next;
        }

        return geometryFactory.createGeneralPolygon(
                boundaryPoints, new ArrayList<>(points), new ArrayList<>(edges));
    }
}
