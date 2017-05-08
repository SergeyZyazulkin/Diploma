package zsp.diploma.mintriang.algorithm.step;

import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;

import java.util.List;

public interface BaseTriangulationBuilder {

    Triangulation buildBaseTriangulation(GeometryFactory geometryFactory, List<Point> points)
            throws TriangulationException;
}
