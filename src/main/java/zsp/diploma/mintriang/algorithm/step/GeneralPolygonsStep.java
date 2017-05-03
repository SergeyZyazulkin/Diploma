package zsp.diploma.mintriang.algorithm.step;

import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.ConvexHull;
import zsp.diploma.mintriang.model.geometry.GeneralPolygon;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;

import java.util.List;

public interface GeneralPolygonsStep {

    List<GeneralPolygon> buildGeneralPolygons(GeometryFactory geometryFactory, ConvexHull convexHull)
            throws TriangulationException;
}
