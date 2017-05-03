package zsp.diploma.mintriang.model.geometry;

import java.util.List;

public interface Triangulation {

    List<Point> getPoints();

    List<Edge> getEdges();

    double getLength();
}
