package zsp.diploma.mintriang.algorithm;

import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;

import java.util.List;

public interface TriangulationAlgorithm {

    Triangulation triangulate(List<Point> points) throws TriangulationException;
}
