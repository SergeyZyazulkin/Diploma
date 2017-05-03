package zsp.diploma.mintriang.algorithm.step;

import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.GeneralPolygon;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;
import zsp.diploma.mintriang.model.geometry.Triangulation;

public interface GeneralPolygonTriangulationStep {

    Triangulation buildTriangulation(GeometryFactory geometryFactory, GeneralPolygon generalPolygon)
            throws TriangulationException;
}
