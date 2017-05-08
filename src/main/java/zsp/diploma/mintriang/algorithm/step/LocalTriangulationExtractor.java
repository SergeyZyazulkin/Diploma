package zsp.diploma.mintriang.algorithm.step;

import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.GeneralPolygon;
import zsp.diploma.mintriang.model.geometry.Hull;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;
import zsp.diploma.mintriang.model.geometry.Triangulation;

public interface LocalTriangulationExtractor {

    GeneralPolygon extractLocalTriangulation(GeometryFactory geometryFactory, Triangulation triangulation)
            throws TriangulationException;

    void reset();
}
