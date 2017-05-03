package zsp.diploma.mintriang.model.geometry.impl;

import zsp.diploma.mintriang.model.geometry.Edge;
import zsp.diploma.mintriang.model.geometry.GraphEdge;
import zsp.diploma.mintriang.model.geometry.Vertex;

import java.util.ArrayList;
import java.util.List;

public class VertexImpl implements Vertex {

    private Edge edge;
    private boolean visited;
    private List<GraphEdge> ingoingEdges;
    private List<GraphEdge> outgoingEdges;

    public VertexImpl(Edge edge) {
        this.edge = edge;
        this.visited = false;
        this.ingoingEdges = new ArrayList<>();
        this.outgoingEdges = new ArrayList<>();
    }

    @Override
    public Edge getEdge() {
        return edge;
    }

    @Override
    public Vertex addIngoingEdge(GraphEdge graphEdge) {
        ingoingEdges.add(graphEdge);
        return this;
    }

    @Override
    public Vertex addOutgoingEdge(GraphEdge graphEdge) {
        outgoingEdges.add(graphEdge);
        return this;
    }

    @Override
    public List<GraphEdge> getIngoingEdges() {
        return ingoingEdges;
    }

    @Override
    public List<GraphEdge> getOutgoingEdges() {
        return outgoingEdges;
    }

    @Override
    public Vertex setVisited(boolean visited) {
        this.visited = visited;
        return this;
    }

    @Override
    public boolean isVisited() {
        return visited;
    }

    @Override
    public GraphEdge getFirstNotVisited(boolean back) {
        return outgoingEdges.parallelStream()
                .filter(this::isAllowed)
                .findFirst()
                .orElseGet(() -> back ?
                        ingoingEdges.parallelStream()
                                .filter(this::isAllowed)
                                .findFirst()
                                .orElse(null) :
                        null);
    }

    private boolean isAllowed(GraphEdge e) {
        return e.getWeight() > 0 && !e.getAnotherVertex().isVisited();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VertexImpl vertex = (VertexImpl) o;

        if (edge != null ? !edge.equals(vertex.edge) : vertex.edge != null) {
            return false;
        }
        if (ingoingEdges != null ? !ingoingEdges.equals(vertex.ingoingEdges) : vertex.ingoingEdges != null) {
            return false;
        }
        return outgoingEdges != null ? outgoingEdges.equals(vertex.outgoingEdges) : vertex.outgoingEdges == null;
    }

    @Override
    public int hashCode() {
        int result = edge != null ? edge.hashCode() : 0;
        result = 31 * result + (ingoingEdges != null ? ingoingEdges.hashCode() : 0);
        result = 31 * result + (outgoingEdges != null ? outgoingEdges.hashCode() : 0);
        return result;
    }
}
