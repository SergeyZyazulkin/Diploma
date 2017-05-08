package zsp.diploma.mintriang.algorithm.step;

import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;
import zsp.diploma.mintriang.model.geometry.Triangulation;

public interface DelaunayConditionChecker {

    Triangulation improveTriangulation(GeometryFactory geometryFactory, Triangulation baseTriangulation)
        throws TriangulationException;
}
