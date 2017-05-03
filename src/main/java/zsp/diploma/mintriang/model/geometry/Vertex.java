package zsp.diploma.mintriang.model.geometry;

import java.util.List;

public interface Vertex {

    Edge getEdge();

    Vertex addIngoingEdge(GraphEdge graphEdge);

    Vertex addOutgoingEdge(GraphEdge graphEdge);

    List<GraphEdge> getIngoingEdges();

    List<GraphEdge> getOutgoingEdges();

    Vertex setVisited(boolean visited);

    boolean isVisited();

    GraphEdge getFirstNotVisited(boolean back);
}
