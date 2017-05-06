package zsp.diploma.mintriang.model.geometry.impl;

import zsp.diploma.mintriang.model.base.Vector;
import zsp.diploma.mintriang.model.geometry.Edge;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.util.Geometry;

import java.util.*;

public class PointImpl implements Point {

    private Vector coordinates;
    private NavigableSet<Point> edges;
    private Vector data;
    private Point parent;
    private int index;
    private int visited;

    public PointImpl(Vector coordinates) {
       this(coordinates, null);
    }

    private PointImpl(Vector coordinates, Point parent) {
        this.coordinates = coordinates;
        this.parent = parent;
        this.visited = 0;
        this.edges = new TreeSet<>(Comparator.comparingDouble(p -> Geometry.calcAngle(this, p)));
    }

    @Override
    public Vector getCoordinates() {
        return coordinates;
    }

    @Override
    public Vector getData() {
        return data;
    }

    @Override
    public Point setData(Vector data) {
        this.data = data;
        return this;
    }

    @Override
    public Point addEdge(Edge edge) {
        if (equals(edge.getFirstPoint())) {
            edges.add(edge.getSecondPoint());
        } else {
            edges.add(edge.getFirstPoint());
        }

        return this;
    }

    @Override
    public Point removeEdge(Edge edge) {
        if (equals(edge.getFirstPoint())) {
            edges.remove(edge.getSecondPoint());
        } else {
            edges.remove(edge.getFirstPoint());
        }

        return this;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public Point setIndex(int index) {
        this.index = index;
        return this;
    }

    @Override
    public Point getNext(Point point, boolean clockwise) {
        if (clockwise) {
            if (point != null) {
                Point next = edges.lower(point);
                return next != null ? next : edges.last();
            } else {
                return edges.last();
            }
        } else {
            if (point != null) {
                Point next = edges.higher(point);
                return next != null ? next : edges.first();
            } else {
                return edges.first();
            }
        }
    }

    @Override
    public Point createChild() {
        return new PointImpl(coordinates, this);
    }

    @Override
    public Point getParent() {
        return parent;
    }

    @Override
    public int getNeighboursCount() {
        return edges.size();
    }

    @Override
    public int getVisited() {
        return visited;
    }

    @Override
    public Point setVisited(int visited) {
        this.visited = visited;
        return this;
    }

    @Override
    public boolean isVisited() {
        return visited >= edges.size() - 1;
    }

    @Override
    public Point visit() {
        ++visited;
        return this;
    }

    @Override
    public Point clearNeighbours() {
        edges.clear();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Point)) {
            return false;
        }

        Point point = (Point) o;

        return coordinates != null ? coordinates.equals(point.getCoordinates()) : point.getCoordinates() == null;
    }

    @Override
    public int hashCode() {
        return coordinates != null ? coordinates.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", coordinates.getX(), coordinates.getY());
    }

    public Point clonePoint() {
        return new PointImpl(coordinates);
    }
}
