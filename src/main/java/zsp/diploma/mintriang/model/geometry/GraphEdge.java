package zsp.diploma.mintriang.model.geometry;

public interface GraphEdge {

    double getWeight();

    GraphEdge setWeight(double weight);

    GraphEdge getOppositeEdge();

    GraphEdge setOppositeEdge(GraphEdge opposite);

    Vertex getCurrentVertex();

    Vertex getAnotherVertex();
}
