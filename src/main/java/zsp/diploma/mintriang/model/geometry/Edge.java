package zsp.diploma.mintriang.model.geometry;

public interface Edge {

    Point getFirstPoint();

    Point getSecondPoint();

    double getLength();

    Edge addToPoints();

    Edge remove();
}
