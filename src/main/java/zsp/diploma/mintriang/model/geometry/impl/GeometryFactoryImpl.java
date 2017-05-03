package zsp.diploma.mintriang.model.geometry.impl;

import zsp.diploma.mintriang.model.base.Vector;
import zsp.diploma.mintriang.model.geometry.*;

import java.util.*;
import java.util.stream.IntStream;

public class GeometryFactoryImpl implements GeometryFactory {

    @Override
    public Point createPoint(Vector coordinates) {
        return new PointImpl(coordinates);
    }

    @Override
    public Edge createEdge(Point p1, Point p2, boolean real) {
        Edge edge = new EdgeImpl(p1, p2);

        if (real) {
            p1.addEdge(edge);
            p2.addEdge(edge);
        }

        return edge;
    }

    @Override
    public ConvexHull createConvexHull(List<Point> boundaryPoints, List<Point> allPoints, List<Edge> edges) {
        return new TriangulationImpl(boundaryPoints, allPoints, edges);
    }

    @Override
    public GeneralPolygon createGeneralPolygon(ConvexHull convexHull) {
        return (GeneralPolygon) convexHull;
    }

    @Override
    public GeneralPolygon createGeneralPolygon(List<Point> boundaryPoints, List<Point> points, List<Edge> edges) {
        return new TriangulationImpl(boundaryPoints, points, edges);
    }

    @Override
    public Triangulation createTriangulation(GeneralPolygon generalPolygon) {
        return (Triangulation) generalPolygon;
    }

    @Override
    public Triangulation createTriangulation(List<Triangulation> generalPolygons) {
        Set<Point> points = new HashSet<>();
        Set<Edge> edges = new HashSet<>();

        for (Triangulation triangulation : generalPolygons) {
            points.addAll(triangulation.getPoints());
            edges.addAll(triangulation.getEdges());
        }

        return new TriangulationImpl(null, new ArrayList<>(points), new ArrayList<>(edges));
    }

    @Override
    public Triangulation createTriangulation(ConvexHull convexHull) {
        return (Triangulation) convexHull;
    }

    @Override
    public Triangulation createTriangulation(List<Point> points, List<Edge> edges) {
        return new TriangulationImpl(null, points, edges);
    }

    @Override
    public Vertex createVertex(Edge accordance) {
        return new VertexImpl(accordance);
    }

    @Override
    public GraphEdge createGraphEdge(Vertex v1, Vertex v2, double weight, GraphEdge opposite) {
        return new GraphEdgeImpl(v1, v2, weight, opposite);
    }

    @Override
    public DicotNetwork createDicotNetwork(Vertex source, List<Vertex> part1, List<Vertex> part2, Vertex sink) {
        return new DicotNetworkImpl(source, sink, part1, part2);
    }

    @Override
    public Triangulation createTriangulation(DicotNetwork dicotNetwork) {
        List<Point> points = new ArrayList<>(
                Arrays.asList(createPoint(new Vector(0, 0)), createPoint(new Vector(6, 0))));

        List<Edge> edges = new ArrayList<>();
        Map<Vertex, Point> mapping = new HashMap<>();

        IntStream.range(0, dicotNetwork.getPart2().size())
                .forEach(i -> {
                    Point newPoint = createPoint(new Vector(4, i));
                    Vertex vertex = dicotNetwork.getPart2().get(i);
                    mapping.put(vertex, newPoint);
                    points.add(newPoint);
                    edges.add(createEdge(newPoint, points.get(1), true));
                });

        IntStream.range(0, dicotNetwork.getPart1().size())
                .forEach(i -> {
                    Point newPoint = createPoint(new Vector(2, i));
                    points.add(newPoint);
                    edges.add(createEdge(points.get(0), newPoint, true));

                    dicotNetwork.getPart1().get(i).getOutgoingEdges()
                            .forEach(edge -> edges.add(
                                    createEdge(newPoint, mapping.get(edge.getAnotherVertex()), true)));
                });

        return createTriangulation(createGeneralPolygon(null, points, edges));
    }
}
