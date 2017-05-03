package zsp.diploma.mintriang.model.geometry;

import java.util.List;

public interface GeneralPolygon {

    List<Point> getPoints(boolean boundaryFirst);

    List<Point> getBoundaryPoints();

    List<Edge> getEdges();

    GeneralPolygon mergeEdges();

    List<Point> getBypass();

    GeneralPolygon addEdge(Edge edge);
}
