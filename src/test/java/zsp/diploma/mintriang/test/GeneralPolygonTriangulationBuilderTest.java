package zsp.diploma.mintriang.test;

import org.junit.Assert;
import org.junit.Test;
import zsp.diploma.mintriang.algorithm.step.impl.GeneralPolygonTriangulationBuilderImpl;
import zsp.diploma.mintriang.algorithm.step.impl.ConvexHullBuilderImpl;
import zsp.diploma.mintriang.algorithm.step.impl.ConvexGeneralPolygonBuilderImpl;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.base.Pair;
import zsp.diploma.mintriang.model.base.Vector;
import zsp.diploma.mintriang.model.geometry.*;

import java.util.List;

public class GeneralPolygonTriangulationBuilderTest extends BaseTest {

    private void testGeneralPolygonTriangulation(
            List<Point> points, List<Edge> expectedEdges) throws TriangulationException {

        ConvexHullBuilderImpl convexHullBuilderImpl = new ConvexHullBuilderImpl();
        ConvexGeneralPolygonBuilderImpl generalPolygonBuilderImpl = new ConvexGeneralPolygonBuilderImpl();
        GeneralPolygonTriangulationBuilderImpl generalPolygonTriangulationBuilderImpl = new GeneralPolygonTriangulationBuilderImpl();
        Hull convexHull = convexHullBuilderImpl.buildConvexHull(geometryFactory, points);
        GeneralPolygon generalPolygon = generalPolygonBuilderImpl.buildGeneralPolygon(geometryFactory, convexHull);
        Triangulation triangulation = generalPolygonTriangulationBuilderImpl.buildTriangulation(geometryFactory, generalPolygon);
        visualize(triangulation);
        Assert.assertTrue(equal(triangulation.getEdges(), expectedEdges));
    }

    @Test
    public void testTriangle() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(-1, -1), new Vector(-1, 0), new Vector(-1, 1), new Vector(-1, 2),
                new Vector(0, 1), new Vector(1, 0), new Vector(2, -1), new Vector(1, -1), new Vector(0, -1),
                new Vector(0, 0));

        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 5), new Pair<>(5, 6), new Pair<>(6, 7), new Pair<>(7, 8), new Pair<>(8, 0),
                new Pair<>(8, 9), new Pair<>(1, 8), new Pair<>(7, 9), new Pair<>(5, 7), new Pair<>(1, 9),
                new Pair<>(5, 9), new Pair<>(2, 9), new Pair<>(4, 9), new Pair<>(2, 4));

        testGeneralPolygonTriangulation(points, edges);
    }

    @Test
    public void testTriangle2() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(0, 0), new Vector(1, 1), new Vector(2, 2), new Vector(-2, 2),
                new Vector(-1, 1));

        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 0), new Pair<>(1, 4), new Pair<>(1, 3));

        testGeneralPolygonTriangulation(points, edges);
    }

    @Test
    public void testTriangle3() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(-1, -1), new Vector(1, -1), new Vector(0, 1));
        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 0));
        testGeneralPolygonTriangulation(points, edges);
    }

    @Test
    public void testRectangle() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(-1, -1), new Vector(-1, 0), new Vector(-1, 1), new Vector(0, 1),
                new Vector(1, 1), new Vector(1, 0), new Vector(1, -1), new Vector(0, -1), new Vector(0, 0));

        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 5), new Pair<>(5, 6), new Pair<>(6, 7), new Pair<>(7, 0), new Pair<>(7, 8),
                new Pair<>(1, 7), new Pair<>(6, 8), new Pair<>(1, 8), new Pair<>(5, 8), new Pair<>(1, 3),
                new Pair<>(3, 8), new Pair<>(4, 8));

        testGeneralPolygonTriangulation(points, edges);
    }

    @Test
    public void testPolygon() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(1, 0), new Vector(2, 0), new Vector(0, 1), new Vector(1, 0.5),
                new Vector(1.5, 0.5), new Vector(3, 0), new Vector(0, 2), new Vector(1, 2.5), new Vector(1.5, 1),
                new Vector(3, 2), new Vector(1, 3), new Vector(2, 3));

        List<Edge> edges = buildEdges(points, new Pair<>(2, 6), new Pair<>(5, 9), new Pair<>(10, 11), new Pair<>(0, 1),
                new Pair<>(0, 2), new Pair<>(6, 10), new Pair<>(9, 11), new Pair<>(1, 5), new Pair<>(0, 3),
                new Pair<>(3, 4), new Pair<>(4, 8), new Pair<>(7, 10), new Pair<>(0, 4), new Pair<>(1, 4),
                new Pair<>(5, 8), new Pair<>(8, 9), new Pair<>(2, 7), new Pair<>(6, 7), new Pair<>(7, 8),
                new Pair<>(7, 9), new Pair<>(2, 3), new Pair<>(7, 11), new Pair<>(2, 8), new Pair<>(3, 8),
                new Pair<>(1, 8));

        testGeneralPolygonTriangulation(points, edges);
    }
}
