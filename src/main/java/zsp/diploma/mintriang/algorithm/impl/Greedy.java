package zsp.diploma.mintriang.algorithm.impl;

import zsp.diploma.mintriang.algorithm.TriangulationAlgorithm;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.Edge;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;
import zsp.diploma.mintriang.util.Geometry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Greedy implements TriangulationAlgorithm {

    private GeometryFactory geometryFactory;

    public Greedy(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
    }

    @Override
    public Triangulation triangulate(List<Point> points) throws TriangulationException {
        points = checkInput(points);
        List<Edge> edges = buildAllEdges(points);
        List<Edge> triangulationEdges = findTriangulationEdges(edges);
        return buildTriangulation(points, triangulationEdges);
    }

    private List<Edge> buildAllEdges(List<Point> points) {
        List<Edge> edges = new ArrayList<>();
        int pointsSize = points.size();

        for (int i = 0; i < pointsSize; ++i) {
            for (int j = i + 1; j < pointsSize; ++j) {
                edges.add(geometryFactory.createEdge(points.get(i), points.get(j), false));
            }
        }

        return edges;
    }

    private List<Edge> findTriangulationEdges(List<Edge> edges) {
        List<Edge> sorted = edges.parallelStream()
                .sorted(Comparator.comparingDouble(Edge::getLength))
                .collect(Collectors.toList());

        List<Edge> triangulationEdges = new ArrayList<>();

        for (Edge edge : sorted) {
            boolean intersects = false;

            for (Edge existing : triangulationEdges) {
                if (Geometry.isIntersect(edge.getFirstPoint(), edge.getSecondPoint(),
                        existing.getFirstPoint(), existing.getSecondPoint())) {

                    intersects = true;
                    break;
                }
            }

            if (!intersects) {
                triangulationEdges.add(edge);
            }
        }

        return triangulationEdges;
    }

    private Triangulation buildTriangulation(List<Point> points, List<Edge> edges) {
        edges.forEach(Edge::addToPoints);
        return geometryFactory.createTriangulation(points, edges);
    }
}
