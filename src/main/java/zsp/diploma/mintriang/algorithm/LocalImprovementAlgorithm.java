package zsp.diploma.mintriang.algorithm;

import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.Triangulation;

public interface LocalImprovementAlgorithm {

    Triangulation improveTriangulation(Triangulation triangulation) throws TriangulationException;
}
