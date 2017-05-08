package zsp.diploma.mintriang.model.geometry.impl;

import zsp.diploma.mintriang.model.geometry.Edge;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.util.Geometry;

public class EdgeImpl implements Edge {

    private Point p1;
    private Point p2;
    private double length;

    public EdgeImpl(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.length = Geometry.calcDistance(p1, p2);
    }

    @Override
    public Point getFirstPoint() {
        return p1;
    }

    @Override
    public Point getSecondPoint() {
        return p2;
    }

    @Override
    public Edge setFirstPoint(Point point) {
        this.p1 = point;
        return this;
    }

    @Override
    public Edge setSecondPoint(Point point) {
        this.p2 = point;
        return this;
    }

    @Override
    public double getLength() {
        return length;
    }

    @Override
    public Edge addToPoints() {
        p1.addEdge(this);
        p2.addEdge(this);
        return this;
    }

    @Override
    public Edge remove() {
        p1.removeEdge(this);
        p2.removeEdge(this);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || !(o instanceof Edge)) {
            return false;
        }

        Edge edge = (Edge) o;

        return (p1 != null ? p1.equals(edge.getFirstPoint()) : edge.getFirstPoint() == null) &&
                (p2 != null ? p2.equals(edge.getSecondPoint()) : edge.getSecondPoint() == null) ||
                (p1 != null ? p1.equals(edge.getSecondPoint()) : edge.getSecondPoint() == null) &&
                        (p2 != null ? p2.equals(edge.getFirstPoint()) : edge.getFirstPoint() == null);
    }

    @Override
    public int hashCode() {
        int result = p1 != null ? p1.hashCode() : 0;
        result += (p2 != null ? p2.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", p1.toString(), p2.toString());
    }
}
