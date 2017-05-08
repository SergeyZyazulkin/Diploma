package zsp.diploma.mintriang.model.geometry;

import java.util.List;

public interface Hull {

    List<Point> getBoundaryPoints();

    List<Edge> getEdges();
}
