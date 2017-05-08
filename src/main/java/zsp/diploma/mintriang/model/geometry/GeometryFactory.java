package zsp.diploma.mintriang.model.geometry;

import zsp.diploma.mintriang.model.base.Vector;

import java.util.List;

public interface GeometryFactory {

    Point createPoint(Vector coordinates);

    Edge createEdge(Point p1, Point p2, boolean real);

    Hull createHull(List<Point> boundaryPoints, List<Point> allPoints, List<Edge> edges);

    Hull createHull(Triangulation triangulation);

    GeneralPolygon createGeneralPolygon(Hull hull);

    GeneralPolygon createGeneralPolygon(List<Point> boundaryPoints, List<Point> points, List<Edge> edges);

    Triangulation createTriangulation(Hull hull);

    Triangulation createTriangulation(GeneralPolygon generalPolygon);

    Triangulation createTriangulation(List<Triangulation> triangulations);

    Triangulation createTriangulation(DicotNetwork dicotNetwork);

    Triangulation createTriangulation(List<Point> points, List<Edge> edges);

    Vertex createVertex(Edge accordance);

    GraphEdge createGraphEdge(Vertex v1, Vertex v2, double weight, GraphEdge opposite);

    DicotNetwork createDicotNetwork(Vertex source, List<Vertex> part1, List<Vertex> part2, Vertex sink);
}
