package zsp.diploma.mintriang.algorithm.impl;

import zsp.diploma.mintriang.algorithm.TriangulationAlgorithm;
import zsp.diploma.mintriang.algorithm.step.*;
import zsp.diploma.mintriang.exception.AlgorithmIncompleteException;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.*;
import zsp.diploma.mintriang.util.Checker;
import zsp.diploma.mintriang.util.Visualizer;

import java.util.List;

public class Delaunay implements TriangulationAlgorithm {

    private GeometryFactory geometryFactory;
    private BaseTriangulationStep baseTriangulationStep;
    private DelaunayConditionStep delaunayConditionStep;

    public static Builder newBuilder() {
        return new Builder();
    }

    private Delaunay(GeometryFactory geometryFactory,
                     BaseTriangulationStep baseTriangulationStep, DelaunayConditionStep delaunayConditionStep)
            throws AlgorithmIncompleteException {

        this.geometryFactory = geometryFactory;
        this.baseTriangulationStep = baseTriangulationStep;
        this.delaunayConditionStep = delaunayConditionStep;
        checkParameters();
    }

    private void checkParameters() throws AlgorithmIncompleteException {
        if (!Checker.notNull(geometryFactory, baseTriangulationStep, delaunayConditionStep)) {
            throw new AlgorithmIncompleteException();
        }
    }

    @Override
    public Triangulation triangulate(List<Point> points) throws TriangulationException {
        points = checkInput(points);
        Triangulation baseTriangulation = baseTriangulationStep.buildBaseTriangulation(geometryFactory, points);
        Visualizer.visualize(baseTriangulation, "temp.png");
        return delaunayConditionStep.improveTriangulation(geometryFactory, baseTriangulation);
    }

    public static class Builder {

        private GeometryFactory geometryFactory;
        private BaseTriangulationStep baseTriangulationStep;
        private DelaunayConditionStep delaunayConditionStep;

        private Builder() {
        }

        public GeometryFactory getGeometryFactory() {
            return geometryFactory;
        }

        public Builder setGeometryFactory(GeometryFactory geometryFactory) {
            this.geometryFactory = geometryFactory;
            return this;
        }

        public BaseTriangulationStep getBaseTriangulationStep() {
            return baseTriangulationStep;
        }

        public Builder setBaseTriangulationStep(BaseTriangulationStep baseTriangulationStep) {
            this.baseTriangulationStep = baseTriangulationStep;
            return this;
        }

        public DelaunayConditionStep getDelaunayConditionStep() {
            return delaunayConditionStep;
        }

        public Builder setDelaunayConditionStep(DelaunayConditionStep delaunayConditionStep) {
            this.delaunayConditionStep = delaunayConditionStep;
            return this;
        }

        public Delaunay build() throws AlgorithmIncompleteException {
            return new Delaunay(geometryFactory, baseTriangulationStep, delaunayConditionStep);
        }
    }
}
