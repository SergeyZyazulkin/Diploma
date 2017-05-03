package zsp.diploma.mintriang.test;

import org.junit.Assert;
import org.junit.Test;
import zsp.diploma.mintriang.algorithm.impl.Heuristic1;
import zsp.diploma.mintriang.algorithm.step.impl.GeneralPolygonKruskal;
import zsp.diploma.mintriang.algorithm.step.impl.GeneralPolygonTriangulation;
import zsp.diploma.mintriang.algorithm.step.impl.GrahamScan;
import zsp.diploma.mintriang.exception.InvalidInputPointsException;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.base.Pair;
import zsp.diploma.mintriang.model.base.Vector;
import zsp.diploma.mintriang.model.geometry.*;

import java.util.List;

public class Heuristic1Test extends BaseTest {

    private void testHeuristic1(
            List<Point> points, List<Edge> expectedEdges) throws TriangulationException {

        Heuristic1 heuristic1 = Heuristic1.newBuilder()
                .setGeometryFactory(geometryFactory)
                .setConvexHullStep(new GrahamScan())
                .setGeneralPolygonStep(new GeneralPolygonKruskal())
                .setGeneralPolygonTriangulationStep(new GeneralPolygonTriangulation())
                .build();

        Triangulation triangulation = heuristic1.triangulate(points);
        visualize(triangulation);

        if (expectedEdges != null) {
            Assert.assertTrue(equal(triangulation.getEdges(), expectedEdges));
        }
    }

    @Test
    public void testTriangle() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(-1, -1), new Vector(-1, 0), new Vector(-1, 1), new Vector(-1, 2),
                new Vector(0, 1), new Vector(1, 0), new Vector(2, -1), new Vector(1, -1), new Vector(0, -1),
                new Vector(-1, -1), new Vector(0, 0));

        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 5), new Pair<>(5, 6), new Pair<>(6, 7), new Pair<>(7, 8), new Pair<>(8, 9),
                new Pair<>(8, 10), new Pair<>(1, 8), new Pair<>(7, 10), new Pair<>(5, 7), new Pair<>(1, 10),
                new Pair<>(5, 10), new Pair<>(2, 10), new Pair<>(4, 10), new Pair<>(2, 4));

        testHeuristic1(points, edges);
    }

    @Test
    public void testTriangle2() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(0, 0), new Vector(1, 1), new Vector(2, 2), new Vector(-2, 2),
                new Vector(-1, 1));

        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 0), new Pair<>(1, 4), new Pair<>(1, 3));

        testHeuristic1(points, edges);
    }

    @Test
    public void testTriangle3() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(-1, -1), new Vector(1, -1), new Vector(0, 1));
        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 0));
        testHeuristic1(points, edges);
    }

    @Test
    public void testRectangle() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(-1, -1), new Vector(-1, 0), new Vector(-1, 1), new Vector(0, 1),
                new Vector(1, 1), new Vector(1, 0), new Vector(1, -1), new Vector(0, -1), new Vector(0, 0));

        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 5), new Pair<>(5, 6), new Pair<>(6, 7), new Pair<>(7, 0), new Pair<>(7, 8),
                new Pair<>(1, 7), new Pair<>(6, 8), new Pair<>(1, 8), new Pair<>(5, 8), new Pair<>(1, 3),
                new Pair<>(3, 8), new Pair<>(4, 8));

        testHeuristic1(points, edges);
    }

    @Test(expected = InvalidInputPointsException.class)
    public void testNotEnoughPoints() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(-1, -1), new Vector(-1, 0));
        testHeuristic1(points, null);
    }

    @Test(expected = InvalidInputPointsException.class)
    public void testLine() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(1, -1), new Vector(1, 0), new Vector(1, 1), new Vector(1, 2),
                new Vector(1, -1), new Vector(1, 10), new Vector(1, -100), new Vector(1, 1000), new Vector(1, -1000));

        testHeuristic1(points, null);
    }

    @Test(expected = InvalidInputPointsException.class)
    public void testLine2() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(1.12736782, -1.1231), new Vector(1.12736782, 0.12312),
                new Vector(1.12736782, 1.6345), new Vector(1.12736782, 2.32432), new Vector(1.12736782, -1.87567),
                new Vector(1.12736782, 10.6575), new Vector(1.12736782, -100.56756), new Vector(1.12736782, 1000.123),
                new Vector(1.12736782, -1000.7546));

        testHeuristic1(points, null);
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

        testHeuristic1(points, edges);
    }

    @Test
    public void testRandom() throws TriangulationException {
        List<Point> points = getRandomPoints(300);
        testHeuristic1(points, null);
    }
}
