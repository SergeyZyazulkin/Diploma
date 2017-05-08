package zsp.diploma.mintriang.algorithm.step;

import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;
import zsp.diploma.mintriang.model.geometry.Triangulation;

public interface LocalTriangulationCombiner {

    Triangulation mergeLocalTriangulation(
            GeometryFactory geometryFactory, Triangulation base, Triangulation baseLocal, Triangulation improvedLocal)
        throws TriangulationException;
}
