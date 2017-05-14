package zsp.diploma.mintriang.algorithm.impl;

import zsp.diploma.mintriang.algorithm.UnionAlgorithm;
import zsp.diploma.mintriang.algorithm.step.DicotNetworkBuilder;
import zsp.diploma.mintriang.algorithm.step.TriangulationCombiner;
import zsp.diploma.mintriang.exception.AlgorithmIncompleteException;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.DicotNetwork;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;
import zsp.diploma.mintriang.model.geometry.Triangulation;
import zsp.diploma.mintriang.util.Checker;
import zsp.diploma.mintriang.util.Visualizer;

public class TriangulationUnionAlgorithm implements UnionAlgorithm {

    private GeometryFactory geometryFactory;
    private DicotNetworkBuilder dicotNetworkBuilder;
    private TriangulationCombiner triangulationCombiner;

    private TriangulationUnionAlgorithm(GeometryFactory geometryFactory, DicotNetworkBuilder dicotNetworkBuilder,
                                        TriangulationCombiner triangulationCombiner)
            throws AlgorithmIncompleteException {

        this.geometryFactory = geometryFactory;
        this.dicotNetworkBuilder = dicotNetworkBuilder;
        this.triangulationCombiner = triangulationCombiner;
        checkParameters();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    private void checkParameters() throws AlgorithmIncompleteException {
        if (!Checker.notNull(geometryFactory, dicotNetworkBuilder, triangulationCombiner)) {
            throw new AlgorithmIncompleteException();
        }
    }

    @Override
    public Triangulation unite(Triangulation triangulation1, Triangulation triangulation2)
            throws TriangulationException {

        DicotNetwork dicotNetwork = dicotNetworkBuilder.buildDicotNetwork(geometryFactory, triangulation1, triangulation2);
        Triangulation t = triangulationCombiner.buildUnitedTriangulation(geometryFactory, dicotNetwork);
        Visualizer.visualize(t, "zTU.png");
        return t;
    }

    public static class Builder {

        private GeometryFactory geometryFactory;
        private DicotNetworkBuilder dicotNetworkBuilder;
        private TriangulationCombiner triangulationCombiner;

        private Builder() {
        }

        public Builder setGeometryFactory(GeometryFactory geometryFactory) {
            this.geometryFactory = geometryFactory;
            return this;
        }

        public Builder setDicotNetworkBuilder(DicotNetworkBuilder dicotNetworkBuilder) {
            this.dicotNetworkBuilder = dicotNetworkBuilder;
            return this;
        }

        public Builder setTriangulationCombiner(TriangulationCombiner triangulationCombiner) {
            this.triangulationCombiner = triangulationCombiner;
            return this;
        }

        public TriangulationUnionAlgorithm build() throws AlgorithmIncompleteException {
            return new TriangulationUnionAlgorithm(geometryFactory, dicotNetworkBuilder, triangulationCombiner);
        }
    }
}
