package zsp.diploma.mintriang.model.geometry.impl;

import zsp.diploma.mintriang.model.geometry.GraphEdge;
import zsp.diploma.mintriang.model.geometry.Vertex;

public class GraphEdgeImpl implements GraphEdge {

    private Vertex current;
    private Vertex another;
    private double weight;
    private GraphEdge opposite;

    public GraphEdgeImpl(Vertex current, Vertex another, double weight, GraphEdge opposite) {
        this.current = current;
        this.another = another;
        this.weight = weight;
        this.opposite = opposite;

        if (opposite != null) {
            opposite.setOppositeEdge(this);
        }
    }

    @Override
    public Vertex getCurrentVertex() {
        return current;
    }

    @Override
    public Vertex getAnotherVertex() {
        return another;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public GraphEdge getOppositeEdge() {
        return opposite;
    }

    @Override
    public GraphEdge setOppositeEdge(GraphEdge opposite) {
        this.opposite = opposite;
        return this;
    }

    @Override
    public GraphEdge setWeight(double weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GraphEdgeImpl graphEdge = (GraphEdgeImpl) o;

        if (Double.compare(graphEdge.weight, weight) != 0) {
            return false;
        }

        return current == graphEdge.current && another == graphEdge.another;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(weight);
        return (int) (temp ^ (temp >>> 32));
    }
}
