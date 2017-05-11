package zsp.diploma.mintriang.algorithm.impl;

import zsp.diploma.mintriang.model.geometry.Edge;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;

import java.util.Collections;
import java.util.List;

public class RandomTriangulationAlgorithm extends GreedyTriangulationAlgorithm {

    public RandomTriangulationAlgorithm(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    @Override
    protected List<Edge> prepareEdges(List<Edge> edges) {
        Collections.shuffle(edges);
        return edges;
    }
}
