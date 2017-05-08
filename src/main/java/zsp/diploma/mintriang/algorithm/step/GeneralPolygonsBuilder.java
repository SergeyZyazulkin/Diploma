package zsp.diploma.mintriang.algorithm.step;

import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.Hull;
import zsp.diploma.mintriang.model.geometry.GeneralPolygon;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;

import java.util.List;

public interface GeneralPolygonsBuilder {

    List<GeneralPolygon> buildGeneralPolygons(GeometryFactory geometryFactory, Hull convexHull)
            throws TriangulationException;
}
