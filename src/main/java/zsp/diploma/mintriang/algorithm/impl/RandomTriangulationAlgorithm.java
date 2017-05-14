package zsp.diploma.mintriang.algorithm.impl;

import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.Edge;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;
import zsp.diploma.mintriang.util.Visualizer;

import java.util.Collections;
import java.util.List;

public class RandomTriangulationAlgorithm extends GreedyTriangulationAlgorithm {

    public RandomTriangulationAlgorithm(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    @Override
    public Triangulation triangulate(List<Point> points) throws TriangulationException {
        points = checkInput(points);
        List<Edge> edges = buildAllEdges(points);
        List<Edge> prepared = prepareEdges(edges);
        List<Edge> triangulationEdges = findTriangulationEdges(prepared);
        Triangulation t = buildTriangulation(points, triangulationEdges);
        Visualizer.visualize(t, "zRandom.png");
        return t;
    }

    @Override
    protected List<Edge> prepareEdges(List<Edge> edges) {
        Collections.shuffle(edges);
        return edges;
    }
}
