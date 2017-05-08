package zsp.diploma.mintriang.algorithm.step.impl;

import zsp.diploma.mintriang.algorithm.step.DicotNetworkBuilder;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.*;
import zsp.diploma.mintriang.util.Geometry;

import java.util.List;
import java.util.stream.Collectors;

public class DicotNetworkBuilderImpl implements DicotNetworkBuilder {

    @Override
    public DicotNetwork buildDicotNetwork(
            GeometryFactory geometryFactory, Triangulation triangulation1, Triangulation triangulation2)
            throws TriangulationException {

        double maxLength = calcMaxLength(triangulation1, triangulation2);
        List<Vertex> vertices1 = mapIntoVertices(geometryFactory, triangulation1);
        List<Vertex> vertices2 = mapIntoVertices(geometryFactory, triangulation2);
        findIntersections(geometryFactory, vertices1, vertices2);
        Vertex source = addSource(geometryFactory, vertices1, maxLength);
        Vertex sink = addSink(geometryFactory, vertices2, maxLength);
        return geometryFactory.createDicotNetwork(source, vertices1, vertices2, sink);
    }

    private double calcMaxLength(Triangulation triangulation1, Triangulation triangulation2) {
        double maxInTriangulation1 = calcMaxLength(triangulation1);
        double maxInTriangulation2 = calcMaxLength(triangulation2);
        return Math.max(maxInTriangulation1, maxInTriangulation2) + 1;
    }

    private double calcMaxLength(Triangulation triangulationIntegratorImpl) {
        return triangulationIntegratorImpl.getEdges()
                .parallelStream()
                .map(Edge::getLength)
                .max(Double::compareTo)
                .get();
    }

    private List<Vertex> mapIntoVertices(GeometryFactory geometryFactory, Triangulation triangulation) {
        return triangulation.getEdges()
                .parallelStream()
                .map(geometryFactory::createVertex)
                .collect(Collectors.toList());
    }

    private void findIntersections(GeometryFactory geometryFactory, List<Vertex> part1, List<Vertex> part2) {
        part1.forEach(vertex1 -> part2.parallelStream()
                .forEach(vertex2 -> {
                    if (Geometry.isIntersect(vertex1.getEdge().getFirstPoint(),
                            vertex1.getEdge().getSecondPoint(),
                            vertex2.getEdge().getFirstPoint(),
                            vertex2.getEdge().getSecondPoint())) {

                        vertex2.addIngoingEdge(geometryFactory.createGraphEdge(vertex2, vertex1, 0, null));
                    }
                }));

        part2.forEach(vertex2 -> vertex2.getIngoingEdges()
                .parallelStream()
                .forEach(edge -> {
                    GraphEdge outgoing = geometryFactory.createGraphEdge(
                            edge.getAnotherVertex(), edge.getCurrentVertex(), Double.MAX_VALUE, edge);

                    edge.getAnotherVertex().addOutgoingEdge(outgoing);
                }));
    }

    private Vertex addSource(GeometryFactory geometryFactory, List<Vertex> part1, double maxLength) {
        Vertex source = geometryFactory.createVertex(null);

        part1.forEach(vertex -> {
            double weight = maxLength - vertex.getEdge().getLength();
            GraphEdge edge = geometryFactory.createGraphEdge(source, vertex, weight, null);
            source.addOutgoingEdge(edge);
        });

        source.getOutgoingEdges()
                .parallelStream()
                .forEach(edge -> {
                    GraphEdge ingoingEdge = geometryFactory.createGraphEdge(edge.getAnotherVertex(), source, 0, edge);
                    edge.getAnotherVertex().addIngoingEdge(ingoingEdge);
                });

        return source;
    }

    private Vertex addSink(GeometryFactory geometryFactory, List<Vertex> part2, double maxLength) {
        Vertex sink = geometryFactory.createVertex(null);

        part2.forEach(vertex -> {
            GraphEdge edge = geometryFactory.createGraphEdge(sink, vertex, 0, null);
            sink.addIngoingEdge(edge);
        });

        sink.getIngoingEdges()
                .parallelStream()
                .forEach(edge -> {
                    Vertex anotherVertex = edge.getAnotherVertex();
                    double weight = maxLength - anotherVertex.getEdge().getLength();
                    GraphEdge outgoingEdge = geometryFactory.createGraphEdge(anotherVertex, sink, weight, edge);
                    anotherVertex.addOutgoingEdge(outgoingEdge);
                });

        return sink;
    }
}
