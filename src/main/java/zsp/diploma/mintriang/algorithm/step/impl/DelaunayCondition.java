package zsp.diploma.mintriang.algorithm.step.impl;

import zsp.diploma.mintriang.algorithm.step.DelaunayConditionStep;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.*;
import zsp.diploma.mintriang.model.geometry.impl.TriangulationImpl;
import zsp.diploma.mintriang.util.Geometry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DelaunayCondition implements DelaunayConditionStep {

    @Override
    public Triangulation improveTriangulation(GeometryFactory geometryFactory, Triangulation baseTriangulation)
            throws TriangulationException {

        List<Edge> edges = baseTriangulation.getEdges();
        List<Edge> newEdges = new ArrayList<>();

        do {
            edges.addAll(newEdges);
            newEdges.clear();
            Iterator<Edge> iterator = edges.iterator();

            while (iterator.hasNext()) {
                Edge current = iterator.next();
                Edge newEdge = getNewEdge(geometryFactory, current);

                if (newEdge != null) {
                    current.remove();
                    iterator.remove();
                    newEdge.addToPoints();
                    newEdges.add(newEdge);
                }
            }
        } while (!newEdges.isEmpty());

        return baseTriangulation;
    }

    public static Edge getNewEdge(GeometryFactory geometryFactory, Edge edge) {
        Point p11 = edge.getFirstPoint().getNext(edge.getSecondPoint(), true);
        Point p21 = edge.getFirstPoint().getNext(edge.getSecondPoint(), false);
        Point p12 = edge.getSecondPoint().getNext(edge.getFirstPoint(), false);
        Point p22 = edge.getSecondPoint().getNext(edge.getFirstPoint(), true);

        return p11 != p12 || p21 != p22 || p11 == p21 || edge.getLength() <= Geometry.calcDistance(p11, p21) ||
                !Geometry.isIntersect(edge.getFirstPoint(), edge.getSecondPoint(), p11, p21)?
                null : geometryFactory.createEdge(p11, p21, false);
    }
}
