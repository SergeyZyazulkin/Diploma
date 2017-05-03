package zsp.diploma.mintriang.algorithm;

import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.Triangulation;

public interface UnionAlgorithm {

    Triangulation unite(Triangulation triangulation1, Triangulation triangulation2) throws TriangulationException;
}
