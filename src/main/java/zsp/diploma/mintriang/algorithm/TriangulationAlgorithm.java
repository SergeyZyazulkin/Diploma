package zsp.diploma.mintriang.algorithm;

import zsp.diploma.mintriang.exception.InvalidInputPointsException;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;
import zsp.diploma.mintriang.util.Converter;
import zsp.diploma.mintriang.util.Geometry;

import java.util.List;

public interface TriangulationAlgorithm {

    Triangulation triangulate(List<Point> points) throws TriangulationException;

    default List<Point> checkInput(List<Point> points) throws InvalidInputPointsException {
        List<Point> uniquePoints = Converter.toUniqueList(points);

        if (uniquePoints == null || uniquePoints.size() < 3) {
            throw new InvalidInputPointsException();
        }

        Geometry.checkCollinear(uniquePoints, uniquePoints.size());
        return uniquePoints;
    }
}
