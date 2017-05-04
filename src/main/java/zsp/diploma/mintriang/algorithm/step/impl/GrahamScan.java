package zsp.diploma.mintriang.algorithm.step.impl;

import zsp.diploma.mintriang.algorithm.step.ConvexHullStep;
import zsp.diploma.mintriang.exception.InvalidInputPointsException;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.base.Vector;
import zsp.diploma.mintriang.model.geometry.ConvexHull;
import zsp.diploma.mintriang.model.geometry.Edge;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.util.Converter;
import zsp.diploma.mintriang.util.Geometry;

import java.util.*;
import java.util.stream.Collectors;

public class GrahamScan implements ConvexHullStep {

    private Point min;

    @Override
    public ConvexHull buildConvexHull(GeometryFactory geometryFactory, List<Point> points)
            throws TriangulationException {

        min = points.parallelStream().min(this::findMinYMinX).get();
        points.parallelStream().forEach(this::setPolarAngleData);
        points = points.parallelStream().sorted(this::sortByPolarAngle).collect(Collectors.toList());
        int pointsOnConvexHull = findConvexHull(points);
        return buildConvexHull(geometryFactory, points, pointsOnConvexHull);
    }

    private int findMinYMinX(Point p1, Point p2) {
        int compareY = Double.compare(p1.getCoordinates().getY(), p2.getCoordinates().getY());

        if (compareY != 0) {
            return compareY;
        } else {
            return Double.compare(p1.getCoordinates().getX(), p2.getCoordinates().getX());
        }
    }

    private void setPolarAngleData(Point point) {
        double difX = point.getCoordinates().getX() - min.getCoordinates().getX();
        double difY = point.getCoordinates().getY() - min.getCoordinates().getY();

        if (difY != 0) {
            point.setData(new Vector(difX, difY, difX / difY, Geometry.calcDistance(point, min)));
        } else {
            point.setData(new Vector(difX, difY, Double.NaN, Geometry.calcDistance(point, min)));
        }
    }

    private int sortByPolarAngle(Point p1, Point p2) {
        if (p1.getData().get(1) == 0) {
            if (p2.getData().get(1) != 0) {
                return -1;
            } else {
                return Double.compare(p1.getData().get(0), p2.getData().get(0));
            }
        } else if (p2.getData().get(1) == 0) {
            return 1;
        } else {
            int compareCotan = -Double.compare(p1.getData().get(2), p2.getData().get(2));

            if (compareCotan != 0) {
                return compareCotan;
            } else {
                return Double.compare(p1.getData().get(3), p2.getData().get(3));
            }
        }
    }

    private int findConvexHull(List<Point> points) {
        points.add(points.get(0));
        int pointsOnConvexHull = 1;
        int pointsCount = points.size();

        for (int i = 2; i < pointsCount; ++i) {
            while (Geometry.calcTurn(points.get(pointsOnConvexHull - 1),
                    points.get(pointsOnConvexHull), points.get(i)) == Geometry.Turn.CLOCKWISE) {

                if (pointsOnConvexHull > 1) {
                    --pointsOnConvexHull;
                } else if (i == pointsCount - 1) {
                    break;
                } else {
                    ++i;
                }
            }

            ++pointsOnConvexHull;
            Collections.swap(points, pointsOnConvexHull, i);
        }

        return fixSkippedPoints(points, pointsOnConvexHull);
    }

    private int fixSkippedPoints(List<Point> points, int pointsOnConvexHull) {
        int pointsCount = points.size();
        List<Point> skipped = new ArrayList<>();

        for (int i = pointsOnConvexHull + 1; i < pointsCount; ++i) {
            if (Geometry.calcTurn(points.get(0), points.get(pointsOnConvexHull - 1), points.get(i)) ==
                    Geometry.Turn.COLLINEAR) {

                skipped.add(points.get(i));
            }
        }

        List<Point> sorted = skipped.parallelStream()
                .sorted(Comparator.comparingDouble(e -> e.getData().get(3)))
                .collect(Collectors.toList());

        for (int i = sorted.size() - 1; i >= 0; --i) {
            points.remove(sorted.get(i));
            points.add(pointsOnConvexHull, sorted.get(i));
            ++pointsOnConvexHull;
        }

        return pointsOnConvexHull;
    }

    private ConvexHull buildConvexHull(GeometryFactory geometryFactory, List<Point> points, int pointsOnConvexHull) {
        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < pointsOnConvexHull; ++i) {
            edges.add(geometryFactory.createEdge(points.get(i), points.get(i + 1), true));
        }

        points.remove(pointsOnConvexHull);
        List<Point> boundaryPoints = points.subList(0, pointsOnConvexHull);
        return geometryFactory.createConvexHull(boundaryPoints, points, edges);
    }
}
