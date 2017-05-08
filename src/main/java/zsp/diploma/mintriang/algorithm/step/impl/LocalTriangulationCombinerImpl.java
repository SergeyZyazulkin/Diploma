package zsp.diploma.mintriang.algorithm.step.impl;

import zsp.diploma.mintriang.algorithm.step.LocalTriangulationCombiner;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.Edge;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;

import java.util.*;
import java.util.stream.Collectors;

public class LocalTriangulationCombinerImpl implements LocalTriangulationCombiner {

    @Override
    public Triangulation mergeLocalTriangulation(
            GeometryFactory geometryFactory, Triangulation base, Triangulation baseLocal, Triangulation improvedLocal)
            throws TriangulationException {

        Set<Edge> oldLocal = new HashSet<>(baseLocal.getEdges());
        Set<Edge> newLocal = new HashSet<>(improvedLocal.getEdges());

        List<Edge> missing = oldLocal.parallelStream()
                .filter(e -> !newLocal.contains(e))
                .collect(Collectors.toList());

        missing.forEach(Edge::remove);
        List<Edge> allEdges = base.getEdges();
        allEdges.removeAll(missing);

        List<Edge> adding = newLocal.parallelStream()
                .filter(e -> !oldLocal.contains(e))
                .collect(Collectors.toList());

        adding.forEach(Edge::addToPoints);
        allEdges.addAll(adding);
        return base;
    }

    private boolean isMissing(Edge edge, Set<Point> localPoints, Set<Edge> localEdges) {
        return localPoints.contains(edge.getFirstPoint()) && localPoints.contains(edge.getSecondPoint()) &&
                !localEdges.contains(edge);
    }
}
