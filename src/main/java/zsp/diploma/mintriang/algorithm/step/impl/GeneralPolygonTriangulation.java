package zsp.diploma.mintriang.algorithm.step.impl;

import zsp.diploma.mintriang.algorithm.step.GeneralPolygonTriangulationStep;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.base.Pair;
import zsp.diploma.mintriang.model.geometry.*;
import zsp.diploma.mintriang.util.Geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneralPolygonTriangulation implements GeneralPolygonTriangulationStep {

    @Override
    public Triangulation buildTriangulation(GeometryFactory geometryFactory, GeneralPolygon generalPolygon)
            throws TriangulationException {

        List<Point> bypass = generalPolygon.getBypass();
        double[][] mt = checkEdges(bypass, generalPolygon.getEdges());
        int[][] parents = calcMT(mt, bypass);
        return buildTriangulation(geometryFactory, generalPolygon, bypass, parents);
    }

    private double[][] checkEdges(List<Point> bypass, List<Edge> existingEdges) {
        int pointsCount = bypass.size();
        double[][] mt = new double[pointsCount - 1][];

        for (int i = 0; i < pointsCount - 1; ++i) {
            mt[i] = new double[pointsCount];
            Point point1 = bypass.get(i);

            for (int j = i + 1; j < pointsCount; ++j) {
                Point point2 = bypass.get(j);

                if (!isAdjacent(i, j, pointsCount)) {
                    if (point1.getParent() == point2.getParent()) {
                        mt[i][j] = Double.POSITIVE_INFINITY;
                    } else if (!Geometry.isInside(bypass.get(previous(i, pointsCount)), point1,
                            bypass.get(next(i, pointsCount)), point2, Geometry.Turn.CLOCKWISE) ||
                            !Geometry.isInside(bypass.get(previous(j, pointsCount)), point2,
                                    bypass.get(next(j, pointsCount)), point1, Geometry.Turn.CLOCKWISE)) {

                        mt[i][j] = Double.POSITIVE_INFINITY;
                    } else {
                        for (Edge edge : existingEdges) {
                            if (Geometry.isIntersect(point1, point2, edge.getFirstPoint(), edge.getSecondPoint())) {
                                mt[i][j] = Double.POSITIVE_INFINITY;
                                break;
                            }
                        }
                    }
                }
            }
        }

        return mt;
    }

    private int previous(int i, int pointsCount) {
        return i > 0 ? i - 1 : pointsCount - 1;
    }

    private int next(int i, int pointsCount) {
        return i < pointsCount - 1 ? i + 1 : 0;
    }

    private boolean isAdjacent(int i1, int i2) {
        return Math.abs(i1 - i2) == 1;
    }

    private boolean isAdjacent(int i1, int i2, int pointsCount) {
        return next(i1, pointsCount) == i2 || next(i2, pointsCount) == i1;
    }

    private int[][] calcMT(double[][] mt, List<Point> bypass) {
        int pointsCount = bypass.size();
        int[][] parents = new int[mt.length][];

        for (int i = pointsCount - 2; i >= 0; --i) {
            parents[i] = new int[mt[i].length];
            Arrays.fill(parents[i], -1);

            for (int j = i + 1; j < pointsCount; ++j) {
                if (isAdjacent(i, j)) {
                    mt[i][j] = Geometry.calcDistance(bypass.get(i), bypass.get(j));
                } else if (Double.isFinite(mt[i][j])) {
                    double min = mt[i][i + 1] + mt[i + 1][j];
                    int indexMin = i + 1;

                    for (int k = i + 2; k < j; ++k) {
                        double checking = mt[i][k] + mt[k][j];

                        if (min > checking) {
                            min = checking;
                            indexMin = k;
                        }
                    }

                    mt[i][j] = Geometry.calcDistance(bypass.get(i), bypass.get(j)) + min;
                    parents[i][j] = indexMin;
                }
            }
        }

        return parents;
    }

    private Triangulation buildTriangulation(
            GeometryFactory geometryFactory, GeneralPolygon generalPolygon, List<Point> bypass, int[][] parents) {

        List<Pair<Integer, Integer>> addingEdges = new ArrayList<>();
        addingEdges.add(new Pair<>(0, bypass.size() - 1));

        while (!addingEdges.isEmpty()) {
            Pair<Integer, Integer> current = addingEdges.remove(addingEdges.size() - 1);
            int parent = parents[current.getValue1()][current.getValue2()];

            if (parent != -1) {
                addEdge(current.getValue1(), current.getValue2(), bypass, generalPolygon, geometryFactory);
                addingEdges.add(new Pair<>(current.getValue1(), parent));
                addingEdges.add(new Pair<>(parent, current.getValue2()));
            }
        }

        return geometryFactory.createTriangulation(generalPolygon);
    }

    private void addEdge(
            int i, int j, List<Point> points, GeneralPolygon generalPolygon, GeometryFactory geometryFactory) {

        if (!isAdjacent(i, j, points.size())) {
            generalPolygon.addEdge(
                    geometryFactory.createEdge(points.get(i).getParent(), points.get(j).getParent(), true));
        }
    }
}
