package zsp.diploma.mintriang.algorithm.impl;

import zsp.diploma.mintriang.algorithm.TriangulationAlgorithm;
import zsp.diploma.mintriang.algorithm.step.ConvexHullBuilder;
import zsp.diploma.mintriang.algorithm.step.GeneralPolygonTriangulationBuilder;
import zsp.diploma.mintriang.algorithm.step.GeneralPolygonsBuilder;
import zsp.diploma.mintriang.exception.AlgorithmIncompleteException;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.*;
import zsp.diploma.mintriang.util.Checker;

import java.util.List;
import java.util.stream.Collectors;

public class MTHeuristic2 implements TriangulationAlgorithm {

    private GeometryFactory geometryFactory;
    private ConvexHullBuilder convexHullBuilder;
    private GeneralPolygonsBuilder generalPolygonsBuilder;
    private GeneralPolygonTriangulationBuilder generalPolygonTriangulationBuilder;

    private MTHeuristic2(GeometryFactory geometryFactory, ConvexHullBuilder convexHullBuilder,
                         GeneralPolygonsBuilder generalPolygonsBuilder,
                         GeneralPolygonTriangulationBuilder generalPolygonTriangulationBuilder)
            throws AlgorithmIncompleteException {

        this.geometryFactory = geometryFactory;
        this.convexHullBuilder = convexHullBuilder;
        this.generalPolygonsBuilder = generalPolygonsBuilder;
        this.generalPolygonTriangulationBuilder = generalPolygonTriangulationBuilder;
        checkParameters();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    private void checkParameters() throws AlgorithmIncompleteException {
        if (!Checker.notNull(geometryFactory, convexHullBuilder, generalPolygonsBuilder, generalPolygonTriangulationBuilder)) {
            throw new AlgorithmIncompleteException();
        }
    }

    @Override
    public Triangulation triangulate(List<Point> points) throws TriangulationException {
        points = checkInput(points);
        Hull convexHull = convexHullBuilder.buildConvexHull(geometryFactory, points);
        List<GeneralPolygon> generalPolygons = generalPolygonsBuilder.buildGeneralPolygons(geometryFactory, convexHull);

        List<Triangulation> triangulations = generalPolygons.parallelStream()
                .map(gp -> {
                    try {
                        return generalPolygonTriangulationBuilder.buildTriangulation(geometryFactory, gp);
                    } catch (TriangulationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        return geometryFactory.createTriangulation(triangulations);
    }

    public static class Builder {

        private GeometryFactory geometryFactory;
        private ConvexHullBuilder convexHullBuilder;
        private GeneralPolygonsBuilder generalPolygonsBuilder;
        private GeneralPolygonTriangulationBuilder generalPolygonTriangulationBuilder;

        private Builder() {
        }

        public Builder setGeometryFactory(GeometryFactory geometryFactory) {
            this.geometryFactory = geometryFactory;
            return this;
        }

        public Builder setConvexHullBuilder(ConvexHullBuilder convexHullBuilder) {
            this.convexHullBuilder = convexHullBuilder;
            return this;
        }

        public Builder setGeneralPolygonsBuilder(GeneralPolygonsBuilder generalPolygonsBuilder) {
            this.generalPolygonsBuilder = generalPolygonsBuilder;
            return this;
        }

        public Builder setGeneralPolygonTriangulationBuilder(GeneralPolygonTriangulationBuilder
                                                                     generalPolygonTriangulationBuilder) {
            this.generalPolygonTriangulationBuilder = generalPolygonTriangulationBuilder;
            return this;
        }


        public MTHeuristic2 build() throws AlgorithmIncompleteException {
            return new MTHeuristic2(
                    geometryFactory, convexHullBuilder, generalPolygonsBuilder, generalPolygonTriangulationBuilder);
        }
    }
}
