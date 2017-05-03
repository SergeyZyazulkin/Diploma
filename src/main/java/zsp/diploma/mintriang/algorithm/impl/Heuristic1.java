package zsp.diploma.mintriang.algorithm.impl;

import zsp.diploma.mintriang.algorithm.TriangulationAlgorithm;
import zsp.diploma.mintriang.algorithm.step.ConvexHullStep;
import zsp.diploma.mintriang.algorithm.step.GeneralPolygonStep;
import zsp.diploma.mintriang.algorithm.step.GeneralPolygonTriangulationStep;
import zsp.diploma.mintriang.exception.AlgorithmIncompleteException;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.*;
import zsp.diploma.mintriang.util.Checker;
import zsp.diploma.mintriang.util.Converter;

import java.util.List;

public class Heuristic1 implements TriangulationAlgorithm {

    private GeometryFactory geometryFactory;
    private ConvexHullStep convexHullStep;
    private GeneralPolygonStep generalPolygonStep;
    private GeneralPolygonTriangulationStep generalPolygonTriangulationStep;

    public static Builder newBuilder() {
        return new Builder();
    }

    private Heuristic1(GeometryFactory geometryFactory, ConvexHullStep convexHullStep, GeneralPolygonStep
            generalPolygonStep, GeneralPolygonTriangulationStep generalPolygonTriangulationStep)
            throws AlgorithmIncompleteException {

        this.geometryFactory = geometryFactory;
        this.convexHullStep = convexHullStep;
        this.generalPolygonStep = generalPolygonStep;
        this.generalPolygonTriangulationStep = generalPolygonTriangulationStep;
        checkParameters();
    }

    private void checkParameters() throws AlgorithmIncompleteException {
        if (!Checker.notNull(geometryFactory, convexHullStep, generalPolygonStep, generalPolygonTriangulationStep)) {
            throw new AlgorithmIncompleteException();
        }
    }

    @Override
    public Triangulation triangulate(List<Point> points) throws TriangulationException {
        ConvexHull convexHull = convexHullStep.buildConvexHull(geometryFactory, points);
        GeneralPolygon generalPolygon = generalPolygonStep.buildGeneralPolygon(geometryFactory, convexHull);
        return generalPolygonTriangulationStep.buildTriangulation(geometryFactory, generalPolygon);
    }

    public static class Builder {

        private GeometryFactory geometryFactory;
        private ConvexHullStep convexHullStep;
        private GeneralPolygonStep generalPolygonStep;
        private GeneralPolygonTriangulationStep generalPolygonTriangulationStep;

        private Builder() {
        }

        public Builder setGeometryFactory(GeometryFactory geometryFactory) {
            this.geometryFactory = geometryFactory;
            return this;
        }

        public Builder setConvexHullStep(ConvexHullStep convexHullStep) {
            this.convexHullStep = convexHullStep;
            return this;
        }

        public Builder setGeneralPolygonStep(GeneralPolygonStep generalPolygonStep) {
            this.generalPolygonStep = generalPolygonStep;
            return this;
        }

        public Builder setGeneralPolygonTriangulationStep(
                GeneralPolygonTriangulationStep generalPolygonTriangulationStep) {

            this.generalPolygonTriangulationStep = generalPolygonTriangulationStep;
            return this;
        }

        public Heuristic1 build() throws AlgorithmIncompleteException {
            return new Heuristic1(geometryFactory, convexHullStep, generalPolygonStep, generalPolygonTriangulationStep);
        }
    }
}
