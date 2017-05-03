package zsp.diploma.mintriang.algorithm.step;

import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.ConvexHull;
import zsp.diploma.mintriang.model.geometry.GeneralPolygon;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;

public interface GeneralPolygonStep {

    GeneralPolygon buildGeneralPolygon(GeometryFactory geometryFactory, ConvexHull convexHull) throws TriangulationException;
}
