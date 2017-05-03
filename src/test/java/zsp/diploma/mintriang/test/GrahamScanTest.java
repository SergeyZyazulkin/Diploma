package zsp.diploma.mintriang.test;

import org.junit.Assert;
import org.junit.Test;
import zsp.diploma.mintriang.algorithm.step.impl.GrahamScan;
import zsp.diploma.mintriang.exception.InvalidInputPointsException;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.base.Pair;
import zsp.diploma.mintriang.model.base.Vector;
import zsp.diploma.mintriang.model.geometry.ConvexHull;
import zsp.diploma.mintriang.model.geometry.Edge;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;

import java.util.List;

public class GrahamScanTest extends BaseTest {

    private void testGrahamScan(List<Point> points, List<Edge> expectedEdges) throws TriangulationException {
        GrahamScan grahamScan = new GrahamScan();
        ConvexHull convexHull = grahamScan.buildConvexHull(geometryFactory, points);
        visualize((Triangulation) convexHull);
        Assert.assertTrue(equal(convexHull.getEdges(), expectedEdges));
    }

    @Test
    public void testTriangle() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(-1, -1), new Vector(-1, 0), new Vector(-1, 1), new Vector(-1, 2),
                new Vector(0, 1), new Vector(1, 0), new Vector(2, -1), new Vector(1, -1), new Vector(0, -1),
                new Vector(-1, -1), new Vector(0, 0));

        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 5), new Pair<>(5, 6), new Pair<>(6, 7), new Pair<>(7, 8), new Pair<>(8, 9));

        testGrahamScan(points, edges);
    }

    @Test
    public void testTriangle2() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(0, 0), new Vector(1, 1), new Vector(2, 2), new Vector(-2, 2),
                new Vector(-1, 1));

        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 0));

        testGrahamScan(points, edges);
    }

    @Test
    public void testTriangle3() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(-1, -1), new Vector(1, -1), new Vector(0, 1));
        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 0));
        testGrahamScan(points, edges);
    }

    @Test
    public void testRectangle() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(-1, -1), new Vector(-1, 0), new Vector(-1, 1), new Vector(0, 1),
                new Vector(1, 1), new Vector(1, 0), new Vector(1, -1), new Vector(0, -1), new Vector(0, 0));

        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 5), new Pair<>(5, 6), new Pair<>(6, 7), new Pair<>(7, 0));

        testGrahamScan(points, edges);
    }

    @Test(expected = InvalidInputPointsException.class)
    public void testNotEnoughPoints() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(-1, -1), new Vector(-1, 0));
        testGrahamScan(points, null);
    }

    @Test(expected = InvalidInputPointsException.class)
    public void testLine() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(1, -1), new Vector(1, 0), new Vector(1, 1), new Vector(1, 2),
                new Vector(1, -1), new Vector(1, 10), new Vector(1, -100), new Vector(1, 1000), new Vector(1, -1000));

        testGrahamScan(points, null);
    }

    @Test(expected = InvalidInputPointsException.class)
    public void testLine2() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(1.12736782, -1.1231), new Vector(1.12736782, 0.12312),
                new Vector(1.12736782, 1.6345), new Vector(1.12736782, 2.32432), new Vector(1.12736782, -1.87567),
                new Vector(1.12736782, 10.6575), new Vector(1.12736782, -100.56756), new Vector(1.12736782, 1000.123),
                new Vector(1.12736782, -1000.7546));

        testGrahamScan(points, null);
    }

    @Test
    public void testPolygon() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(1, 0), new Vector(2, 0), new Vector(0, 1), new Vector(1, 1),
                new Vector(2, 1), new Vector(3, 0), new Vector(0, 2), new Vector(1, 2), new Vector(2, 2),
                new Vector(3, 2), new Vector(1, 3), new Vector(2, 3));

        List<Edge> edges = buildEdges(points, new Pair<>(2, 6), new Pair<>(5, 9), new Pair<>(10, 11), new Pair<>(0, 1),
                new Pair<>(0, 2), new Pair<>(6, 10), new Pair<>(9, 11), new Pair<>(1, 5));

        testGrahamScan(points, edges);
    }
}
