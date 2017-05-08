package zsp.diploma.mintriang.algorithm.step.impl;

import zsp.diploma.mintriang.algorithm.step.ConvexGeneralPolygonBuilder;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.base.DisjointDataSet;
import zsp.diploma.mintriang.model.geometry.*;
import zsp.diploma.mintriang.util.Geometry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConvexGeneralPolygonBuilderImpl implements ConvexGeneralPolygonBuilder {

    @Override
    public GeneralPolygon buildGeneralPolygon(GeometryFactory geometryFactory, Hull convexHull)
            throws TriangulationException {

        GeneralPolygon generalPolygon = geometryFactory.createGeneralPolygon(convexHull);
        List<Point> points = generalPolygon.getPoints(true);
        int boundaryPointsCount = generalPolygon.getBoundaryPoints().size();
        List<Edge> edges = buildAllEdges(geometryFactory, generalPolygon, points, boundaryPointsCount);
        edges = edges.parallelStream().sorted(Comparator.comparingDouble(Edge::getLength)).collect(Collectors.toList());
        DisjointDataSet disjointDataSet = new DisjointDataSet(points.size());
        mergeBorder(disjointDataSet, boundaryPointsCount);
        Geometry.kruskal(generalPolygon, disjointDataSet, edges);
        return generalPolygon;

    }

    private List<Edge> buildAllEdges(GeometryFactory geometryFactory,
                                     GeneralPolygon generalPolygon, List<Point> points, int boundaryPointsCount) {

        int pointsCount = points.size();
        List<Edge> edges = new ArrayList<>();
        IntStream.range(0, pointsCount).parallel().forEach(i -> points.get(i).setIndex(i));

        for (int i = 0; i < pointsCount; ++i) {
            for (int j = Math.max(i + 1, boundaryPointsCount); j < pointsCount; ++j) {
                Point point1 = points.get(i);
                Point point2 = points.get(j);

                if (isSuitable(generalPolygon, point1, point2)) {
                    edges.add(geometryFactory.createEdge(points.get(i), points.get(j), false));
                }
            }
        }

        return edges;
    }

    protected boolean isSuitable(GeneralPolygon generalPolygon, Point point1, Point point2) {
        return true;
    }

    private void mergeBorder(DisjointDataSet disjointDataSet, int boundaryPointsCount) {
        for (int i = 1; i < boundaryPointsCount; ++i) {
            disjointDataSet.merge(0, i);
        }
    }
}
