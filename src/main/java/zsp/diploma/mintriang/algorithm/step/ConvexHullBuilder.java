package zsp.diploma.mintriang.algorithm.step;

import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.Hull;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;
import zsp.diploma.mintriang.model.geometry.Point;

import java.util.List;

public interface ConvexHullBuilder {

    Hull buildConvexHull(GeometryFactory geometryFactory, List<Point> points) throws TriangulationException;
}
