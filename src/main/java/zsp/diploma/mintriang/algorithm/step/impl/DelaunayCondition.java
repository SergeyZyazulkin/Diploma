package zsp.diploma.mintriang.algorithm.step.impl;

import zsp.diploma.mintriang.algorithm.step.DelaunayConditionStep;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.Edge;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;
import zsp.diploma.mintriang.util.Geometry;

import java.util.*;

public class DelaunayCondition implements DelaunayConditionStep {

    public static List<Edge> getNewEdge(GeometryFactory geometryFactory, Edge edge) {
        Point p11 = edge.getFirstPoint().getNext(edge.getSecondPoint(), true);
        Point p21 = edge.getFirstPoint().getNext(edge.getSecondPoint(), false);
        Point p12 = edge.getSecondPoint().getNext(edge.getFirstPoint(), false);
        Point p22 = edge.getSecondPoint().getNext(edge.getFirstPoint(), true);

        if (p11 != p12 || p21 != p22 || p11 == p21 || edge.getLength() <= Geometry.calcDistance(p11, p21) ||
                !Geometry.isIntersect(edge.getFirstPoint(), edge.getSecondPoint(), p11, p21)) {

            return null;
        } else {
            return Arrays.asList(geometryFactory.createEdge(p11, p21, false),
                    geometryFactory.createEdge(edge.getFirstPoint(), p11, false),
                    geometryFactory.createEdge(edge.getFirstPoint(), p21, false),
                    geometryFactory.createEdge(edge.getSecondPoint(), p12, false),
                    geometryFactory.createEdge(edge.getSecondPoint(), p22, false));
        }
    }

    @Override
    public Triangulation improveTriangulation(GeometryFactory geometryFactory, Triangulation baseTriangulation)
            throws TriangulationException {

        List<Edge> baseEdges = baseTriangulation.getEdges();
        Set<Edge> edges = new HashSet<>(baseEdges);
        ArrayDeque<Edge> checking = new ArrayDeque<>(baseEdges);

        while (!checking.isEmpty()) {
            Edge current = checking.poll();

            if (edges.contains(current)) {
                List<Edge> newEdges = getNewEdge(geometryFactory, current);

                if (newEdges != null) {
                    current.remove();
                    edges.remove(current);
                    Edge newEdge = newEdges.get(0);
                    newEdge.addToPoints();
                    edges.add(newEdge);
                    newEdges.stream().skip(1).forEach(checking::add);
                }
            }
        }

        return geometryFactory.createTriangulation(baseTriangulation.getPoints(), new ArrayList<>(edges));
    }
}
