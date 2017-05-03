package zsp.diploma.mintriang.model.geometry;

import java.util.List;

public interface ConvexHull {

    List<Point> getBoundaryPoints();

    List<Edge> getEdges();
}
