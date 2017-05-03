package zsp.diploma.mintriang.algorithm.impl;

import zsp.diploma.mintriang.algorithm.TriangulationAlgorithm;
import zsp.diploma.mintriang.algorithm.step.ConvexHullStep;
import zsp.diploma.mintriang.algorithm.step.GeneralPolygonTriangulationStep;
import zsp.diploma.mintriang.algorithm.step.GeneralPolygonsStep;
import zsp.diploma.mintriang.exception.AlgorithmIncompleteException;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.*;
import zsp.diploma.mintriang.util.Checker;

import java.util.List;
import java.util.stream.Collectors;

public class Heuristic2 implements TriangulationAlgorithm {

    private GeometryFactory geometryFactory;
    private ConvexHullStep convexHullStep;
    private GeneralPolygonsStep generalPolygonsStep;
    private GeneralPolygonTriangulationStep generalPolygonTriangulationStep;

    private Heuristic2(GeometryFactory geometryFactory, ConvexHullStep convexHullStep,
                       GeneralPolygonsStep generalPolygonsStep,
                       GeneralPolygonTriangulationStep generalPolygonTriangulationStep)
            throws AlgorithmIncompleteException {

        this.geometryFactory = geometryFactory;
        this.convexHullStep = convexHullStep;
        this.generalPolygonsStep = generalPolygonsStep;
        this.generalPolygonTriangulationStep = generalPolygonTriangulationStep;
        checkParameters();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    private void checkParameters() throws AlgorithmIncompleteException {
        if (!Checker.notNull(geometryFactory, convexHullStep, generalPolygonsStep, generalPolygonTriangulationStep)) {
            throw new AlgorithmIncompleteException();
        }
    }

    @Override
    public Triangulation triangulate(List<Point> points) throws TriangulationException {
        ConvexHull convexHull = convexHullStep.buildConvexHull(geometryFactory, points);
        List<GeneralPolygon> generalPolygons = generalPolygonsStep.buildGeneralPolygons(geometryFactory, convexHull);

        List<Triangulation> triangulations = generalPolygons.parallelStream()
                .map(gp -> {
                    try {
                        return generalPolygonTriangulationStep.buildTriangulation(geometryFactory, gp);
                    } catch (TriangulationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        return geometryFactory.createTriangulation(triangulations);
    }

    public static class Builder {

        private GeometryFactory geometryFactory;
        private ConvexHullStep convexHullStep;
        private GeneralPolygonsStep generalPolygonsStep;
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

        public Builder setGeneralPolygonsStep(GeneralPolygonsStep generalPolygonsStep) {
            this.generalPolygonsStep = generalPolygonsStep;
            return this;
        }

        public Builder setGeneralPolygonTriangulationStep(GeneralPolygonTriangulationStep
                                                                  generalPolygonTriangulationStep) {
            this.generalPolygonTriangulationStep = generalPolygonTriangulationStep;
            return this;
        }


        public Heuristic2 build() throws AlgorithmIncompleteException {
            return new Heuristic2(
                    geometryFactory, convexHullStep, generalPolygonsStep, generalPolygonTriangulationStep);
        }
    }
}
