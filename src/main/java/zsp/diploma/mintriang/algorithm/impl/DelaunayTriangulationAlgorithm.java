package zsp.diploma.mintriang.algorithm.impl;

import zsp.diploma.mintriang.algorithm.TriangulationAlgorithm;
import zsp.diploma.mintriang.algorithm.step.BaseTriangulationBuilder;
import zsp.diploma.mintriang.algorithm.step.DelaunayConditionChecker;
import zsp.diploma.mintriang.exception.AlgorithmIncompleteException;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;
import zsp.diploma.mintriang.util.Checker;

import java.util.List;

public class DelaunayTriangulationAlgorithm implements TriangulationAlgorithm {

    private GeometryFactory geometryFactory;
    private BaseTriangulationBuilder baseTriangulationBuilder;
    private DelaunayConditionChecker delaunayConditionChecker;

    private DelaunayTriangulationAlgorithm(GeometryFactory geometryFactory,
                                           BaseTriangulationBuilder baseTriangulationBuilder,
                                           DelaunayConditionChecker delaunayConditionChecker)
            throws AlgorithmIncompleteException {

        this.geometryFactory = geometryFactory;
        this.baseTriangulationBuilder = baseTriangulationBuilder;
        this.delaunayConditionChecker = delaunayConditionChecker;
        checkParameters();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    private void checkParameters() throws AlgorithmIncompleteException {
        if (!Checker.notNull(geometryFactory, baseTriangulationBuilder, delaunayConditionChecker)) {
            throw new AlgorithmIncompleteException();
        }
    }

    @Override
    public Triangulation triangulate(List<Point> points) throws TriangulationException {
        points = checkInput(points);
        Triangulation baseTriangulation = baseTriangulationBuilder.buildBaseTriangulation(geometryFactory, points);
        return delaunayConditionChecker.improveTriangulation(geometryFactory, baseTriangulation);
    }

    public static class Builder {

        private GeometryFactory geometryFactory;
        private BaseTriangulationBuilder baseTriangulationBuilder;
        private DelaunayConditionChecker delaunayConditionChecker;

        private Builder() {
        }

        public GeometryFactory getGeometryFactory() {
            return geometryFactory;
        }

        public Builder setGeometryFactory(GeometryFactory geometryFactory) {
            this.geometryFactory = geometryFactory;
            return this;
        }

        public BaseTriangulationBuilder getBaseTriangulationBuilder() {
            return baseTriangulationBuilder;
        }

        public Builder setBaseTriangulationBuilder(BaseTriangulationBuilder baseTriangulationBuilder) {
            this.baseTriangulationBuilder = baseTriangulationBuilder;
            return this;
        }

        public DelaunayConditionChecker getDelaunayConditionChecker() {
            return delaunayConditionChecker;
        }

        public Builder setDelaunayConditionChecker(DelaunayConditionChecker delaunayConditionChecker) {
            this.delaunayConditionChecker = delaunayConditionChecker;
            return this;
        }

        public DelaunayTriangulationAlgorithm build() throws AlgorithmIncompleteException {
            return new DelaunayTriangulationAlgorithm(geometryFactory, baseTriangulationBuilder,
                    delaunayConditionChecker);
        }
    }
}
