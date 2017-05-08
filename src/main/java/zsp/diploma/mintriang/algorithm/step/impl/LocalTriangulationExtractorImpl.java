package zsp.diploma.mintriang.algorithm.step.impl;

import zsp.diploma.mintriang.algorithm.step.GeneralPolygonsBuilder;
import zsp.diploma.mintriang.algorithm.step.LocalTriangulationExtractor;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LocalTriangulationExtractorImpl implements LocalTriangulationExtractor {

    private boolean first = true;
    private int current = 1;
    private List<GeneralPolygon> generalPolygons;

    @Override
    public GeneralPolygon extractLocalTriangulation(GeometryFactory geometryFactory, Triangulation triangulation)
            throws TriangulationException {

        if (first) {
            first = false;
            generalPolygons = buildLocalGeneralPolygons(geometryFactory, triangulation);
            return generalPolygons.get(0);
        } else {
            return current < generalPolygons.size() ? generalPolygons.get(current++) : null;
        }
    }

    @Override
    public void reset() {
        first = true;
        current = 1;
        generalPolygons = null;
    }

    private List<GeneralPolygon> buildLocalGeneralPolygons(
            GeometryFactory geometryFactory, Triangulation triangulation)
            throws TriangulationException {

        List<Point> points = triangulation.getPoints().parallelStream()
                .unordered()
                .map(Point::clonePoint)
                .collect(Collectors.toList());

        List<Edge> edges = triangulation.getEdges().parallelStream()
                .unordered()
                .map(e ->
                        geometryFactory.createEdge(e.getFirstPoint().getChild(), e.getSecondPoint().getChild(), false))
                .collect(Collectors.toList());

        Hull convexHull = new ConvexHullBuilderImpl().buildConvexHull(geometryFactory, points);

        GeneralPolygonsBuilder localPolygonsBuilder = new GeneralPolygonsBuilderImpl() {
            @Override
            protected List<Edge> buildAllEdges(GeometryFactory geometryFactory, List<Point> points) {
                IntStream.range(0, points.size())
                        .unordered()
                        .parallel()
                        .forEach(i -> points.get(i).setIndex(i));

                return edges;
            }
        };

        return localPolygonsBuilder.buildGeneralPolygons(geometryFactory, convexHull);
    }
}
