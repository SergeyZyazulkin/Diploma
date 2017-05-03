package zsp.diploma.mintriang.algorithm.impl;

import zsp.diploma.mintriang.algorithm.UnionAlgorithm;
import zsp.diploma.mintriang.algorithm.step.DicotNetworkStep;
import zsp.diploma.mintriang.algorithm.step.UnitedTriangulationStep;
import zsp.diploma.mintriang.exception.AlgorithmIncompleteException;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.DicotNetwork;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;
import zsp.diploma.mintriang.model.geometry.Triangulation;
import zsp.diploma.mintriang.util.Checker;

public class TriangulationUnion implements UnionAlgorithm {

    private GeometryFactory geometryFactory;
    private DicotNetworkStep dicotNetworkStep;
    private UnitedTriangulationStep unitedTriangulationStep;

    private TriangulationUnion(GeometryFactory geometryFactory, DicotNetworkStep dicotNetworkStep,
                               UnitedTriangulationStep unitedTriangulationStep)
            throws AlgorithmIncompleteException {

        this.geometryFactory = geometryFactory;
        this.dicotNetworkStep = dicotNetworkStep;
        this.unitedTriangulationStep = unitedTriangulationStep;
        checkParameters();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    private void checkParameters() throws AlgorithmIncompleteException {
        if (!Checker.notNull(geometryFactory, dicotNetworkStep, unitedTriangulationStep)) {
            throw new AlgorithmIncompleteException();
        }
    }

    @Override
    public Triangulation unite(Triangulation triangulation1, Triangulation triangulation2)
            throws TriangulationException {

        DicotNetwork dicotNetwork = dicotNetworkStep.buildDicotNetwork(geometryFactory, triangulation1, triangulation2);
        return unitedTriangulationStep.buildUnitedTriangulation(geometryFactory, dicotNetwork);
    }

    public static class Builder {

        private GeometryFactory geometryFactory;
        private DicotNetworkStep dicotNetworkStep;
        private UnitedTriangulationStep unitedTriangulationStep;

        private Builder() {
        }

        public Builder setGeometryFactory(GeometryFactory geometryFactory) {
            this.geometryFactory = geometryFactory;
            return this;
        }

        public Builder setDicotNetworkStep(DicotNetworkStep dicotNetworkStep) {
            this.dicotNetworkStep = dicotNetworkStep;
            return this;
        }

        public Builder setUnitedTriangulationStep(UnitedTriangulationStep unitedTriangulationStep) {
            this.unitedTriangulationStep = unitedTriangulationStep;
            return this;
        }

        public TriangulationUnion build() throws AlgorithmIncompleteException {
            return new TriangulationUnion(geometryFactory, dicotNetworkStep, unitedTriangulationStep);
        }
    }
}
