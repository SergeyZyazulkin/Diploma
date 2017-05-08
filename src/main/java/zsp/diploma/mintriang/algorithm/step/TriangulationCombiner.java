package zsp.diploma.mintriang.algorithm.step;

import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.DicotNetwork;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;
import zsp.diploma.mintriang.model.geometry.Triangulation;

public interface TriangulationCombiner {

    Triangulation buildUnitedTriangulation(GeometryFactory geometryFactory, DicotNetwork dicotNetwork)
            throws TriangulationException;
}
