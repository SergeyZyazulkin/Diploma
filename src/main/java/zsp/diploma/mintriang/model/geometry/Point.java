package zsp.diploma.mintriang.model.geometry;

import zsp.diploma.mintriang.model.base.Vector;

public interface Point {

    Vector getCoordinates();

    Vector getData();

    Point setData(Vector data);

    Point addEdge(Edge edge);

    Point removeEdge(Edge edge);

    int getIndex();

    Point setIndex(int index);

    Point getNext(Point point, boolean clockwise);

    Point createChild();

    Point getParent();

    int getNeighboursCount();

    int getVisited();

    Point setVisited(int visited);

    Point visit();

    boolean isVisited();

    Point clonePoint();

    Point clearNeighbours();
}
