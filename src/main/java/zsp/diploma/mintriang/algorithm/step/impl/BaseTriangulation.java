package zsp.diploma.mintriang.algorithm.step.impl;

import zsp.diploma.mintriang.algorithm.TriangulationAlgorithm;
import zsp.diploma.mintriang.algorithm.impl.Greedy;
import zsp.diploma.mintriang.algorithm.step.BaseTriangulationStep;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;

import java.util.List;

public class BaseTriangulation implements BaseTriangulationStep {

    @Override
    public Triangulation buildBaseTriangulation(GeometryFactory geometryFactory, List<Point> points)
            throws TriangulationException {

        TriangulationAlgorithm greedy = new Greedy(geometryFactory);
        return greedy.triangulate(points);
    }
}
