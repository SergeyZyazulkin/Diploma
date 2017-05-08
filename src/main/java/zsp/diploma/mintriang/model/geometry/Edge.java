package zsp.diploma.mintriang.model.geometry;

public interface Edge {

    Point getFirstPoint();

    Point getSecondPoint();

    Edge setFirstPoint(Point point);

    Edge setSecondPoint(Point point);

    double getLength();

    Edge addToPoints();

    Edge remove();
}
