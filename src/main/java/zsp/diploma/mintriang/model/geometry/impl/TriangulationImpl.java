package zsp.diploma.mintriang.model.geometry.impl;

import zsp.diploma.mintriang.model.geometry.*;
import zsp.diploma.mintriang.util.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TriangulationImpl implements ConvexHull, GeneralPolygon, Triangulation {

    private List<Point> boundaryPoints;
    private List<Point> allPoints;
    private List<Edge> edges;

    public TriangulationImpl(List<Point> boundaryPoints, List<Point> allPoints, List<Edge> edges) {
        this.boundaryPoints = boundaryPoints;
        this.allPoints = allPoints;
        this.edges = edges;
    }

    @Override
    public List<Edge> getEdges() {
        return edges;
    }

    @Override
    public List<Point> getBoundaryPoints() {
        return boundaryPoints;
    }

    @Override
    public List<Point> getPoints(boolean boundaryFirst) {
        return allPoints;
    }

    @Override
    public List<Point> getPoints() {
        return allPoints;
    }

    @Override
    public GeneralPolygon addEdge(Edge edge) {
        edges.add(edge);
        return this;
    }

    @Override
    public List<Point> getBypass() {
        ArrayList<Point> bypass = new ArrayList<>();
        Point temp;
        Point previous = boundaryPoints.get(1);
        Point current = boundaryPoints.get(0);
        Edge firstEdge = new EdgeImpl(previous, current);
        Edge currentEdge;

        do {
            bypass.add(current.createChild());
            temp = current;
            current = current.getNext(previous, false);
            previous = temp;
            currentEdge = new EdgeImpl(previous, current);
        } while (!firstEdge.equals(currentEdge));

        IntStream.range(0, bypass.size()).parallel().forEach(i -> bypass.get(i).setIndex(i));
        return bypass;
    }

    @Override
    public GeneralPolygon mergeEdges() {
        edges = Converter.toUniqueList(edges);
        return this;
    }

    @Override
    public double getLength() {
        return edges.parallelStream()
                .mapToDouble(Edge::getLength)
                .sum();
    }

    @Override
    public Triangulation normalize() {
        allPoints.parallelStream()
                .unordered()
                .forEach(Point::clearNeighbours);

        edges.forEach(Edge::addToPoints);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Edge edge : edges) {
            sb.append(edge).append('\n');
        }

        return sb.toString();
    }
}
