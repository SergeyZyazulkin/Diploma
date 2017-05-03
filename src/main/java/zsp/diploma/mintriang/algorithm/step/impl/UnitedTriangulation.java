package zsp.diploma.mintriang.algorithm.step.impl;

import zsp.diploma.mintriang.algorithm.step.UnitedTriangulationStep;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.*;

import java.util.*;
import java.util.stream.Collectors;

public class UnitedTriangulation implements UnitedTriangulationStep {

    @Override
    public Triangulation buildUnitedTriangulation(GeometryFactory geometryFactory, DicotNetwork dicotNetwork)
            throws TriangulationException {

        enlarge(dicotNetwork);
        List<Vertex> mwis = findMWIS(dicotNetwork);
        return buildTriangulation(geometryFactory, mwis);
    }

    private void enlarge(DicotNetwork dicotNetwork) {
        Deque<GraphEdge> enlargingPath;

        while (!(enlargingPath = findEnlargingPath(dicotNetwork)).isEmpty()) {
            double flow = enlargingPath.parallelStream()
                    .mapToDouble(GraphEdge::getWeight)
                    .min()
                    .getAsDouble();

            enlargingPath.parallelStream()
                    .forEach(e -> {
                        e.setWeight(e.getWeight() - flow);
                        GraphEdge opposite = e.getOppositeEdge();
                        opposite.setWeight(opposite.getWeight() + flow);
                    });
        }
    }

    private Deque<GraphEdge> findEnlargingPath(DicotNetwork dicotNetwork) {
        Deque<Vertex> vertices = new ArrayDeque<>();
        Deque<GraphEdge> path = new ArrayDeque<>();
        vertices.add(dicotNetwork.getSource());
        dicotNetwork.setVisited(false);
        dicotNetwork.getSource().setVisited(true);

        while (!vertices.isEmpty() && vertices.getLast() != dicotNetwork.getSink()) {
            GraphEdge next = vertices.getLast().getFirstNotVisited(true);

            if (next != null) {
                Vertex nextVertex = next.getAnotherVertex();
                nextVertex.setVisited(true);
                vertices.addLast(nextVertex);
                path.addLast(next);
            } else {
                vertices.pollLast();
                path.pollLast();
            }
        }

        return path;
    }

    private List<Vertex> findMWIS(DicotNetwork dicotNetwork) {
        Deque<Vertex> vertices = new ArrayDeque<>();
        vertices.add(dicotNetwork.getSource());
        dicotNetwork.setVisited(false);
        dicotNetwork.getSource().setVisited(true);

        while (!vertices.isEmpty()) {
            GraphEdge next = vertices.getLast().getFirstNotVisited(false);

            if (next != null) {
                next.getAnotherVertex().setVisited(true);
                vertices.addLast(next.getAnotherVertex());
            } else {
                vertices.pollLast();
            }
        }

        List<Vertex> mwis = new ArrayList<>();

        mwis.addAll(dicotNetwork.getPart1().parallelStream()
                .filter(Vertex::isVisited)
                .collect(Collectors.toList()));

        mwis.addAll(dicotNetwork.getPart2().parallelStream()
                .filter(v -> !v.isVisited())
                .collect(Collectors.toList()));

        return mwis;
    }

    private Triangulation buildTriangulation(GeometryFactory geometryFactory, List<Vertex> mwis) {
        List<Edge> edges = mwis.parallelStream()
                .map(Vertex::getEdge)
                .collect(Collectors.toList());

        List<Point> points = edges.parallelStream()
                .map(e -> new Point[] {e.getFirstPoint(), e.getSecondPoint()})
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());

        return geometryFactory.createTriangulation(points, edges);
    }
}
