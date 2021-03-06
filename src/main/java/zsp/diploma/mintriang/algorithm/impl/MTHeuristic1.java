package zsp.diploma.mintriang.algorithm.impl;

import zsp.diploma.mintriang.algorithm.TriangulationAlgorithm;
import zsp.diploma.mintriang.algorithm.step.ConvexHullBuilder;
import zsp.diploma.mintriang.algorithm.step.ConvexGeneralPolygonBuilder;
import zsp.diploma.mintriang.algorithm.step.GeneralPolygonTriangulationBuilder;
import zsp.diploma.mintriang.exception.AlgorithmIncompleteException;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.*;
import zsp.diploma.mintriang.util.Checker;

import java.util.List;

public class MTHeuristic1 implements TriangulationAlgorithm {

    private GeometryFactory geometryFactory;
    private ConvexHullBuilder convexHullBuilder;
    private ConvexGeneralPolygonBuilder convexGeneralPolygonBuilder;
    private GeneralPolygonTriangulationBuilder generalPolygonTriangulationBuilder;

    private MTHeuristic1(GeometryFactory geometryFactory, ConvexHullBuilder convexHullBuilder, ConvexGeneralPolygonBuilder

            convexGeneralPolygonBuilder, GeneralPolygonTriangulationBuilder generalPolygonTriangulationBuilder)
            throws AlgorithmIncompleteException {

        this.geometryFactory = geometryFactory;
        this.convexHullBuilder = convexHullBuilder;
        this.convexGeneralPolygonBuilder = convexGeneralPolygonBuilder;
        this.generalPolygonTriangulationBuilder = generalPolygonTriangulationBuilder;
        checkParameters();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    private void checkParameters() throws AlgorithmIncompleteException {
        if (!Checker.notNull(geometryFactory, convexHullBuilder, convexGeneralPolygonBuilder,
                generalPolygonTriangulationBuilder)) {

            throw new AlgorithmIncompleteException();
        }
    }

    @Override
    public Triangulation triangulate(List<Point> points) throws TriangulationException {
        points = checkInput(points);
        Hull convexHull = convexHullBuilder.buildConvexHull(geometryFactory, points);
        GeneralPolygon generalPolygon = convexGeneralPolygonBuilder.buildGeneralPolygon(geometryFactory, convexHull);
        return generalPolygonTriangulationBuilder.buildTriangulation(geometryFactory, generalPolygon);
    }

    public static class Builder {

        private GeometryFactory geometryFactory;
        private ConvexHullBuilder convexHullBuilder;
        private ConvexGeneralPolygonBuilder convexGeneralPolygonBuilder;
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

        public Builder setConvexGeneralPolygonBuilder(ConvexGeneralPolygonBuilder convexGeneralPolygonBuilder) {
            this.convexGeneralPolygonBuilder = convexGeneralPolygonBuilder;
            return this;
        }

        public Builder setGeneralPolygonTriangulationBuilder(
                GeneralPolygonTriangulationBuilder generalPolygonTriangulationBuilder) {

            this.generalPolygonTriangulationBuilder = generalPolygonTriangulationBuilder;
            return this;
        }

        public MTHeuristic1 build() throws AlgorithmIncompleteException {
            return new MTHeuristic1(geometryFactory, convexHullBuilder, convexGeneralPolygonBuilder,
                    generalPolygonTriangulationBuilder);
        }
    }
}
