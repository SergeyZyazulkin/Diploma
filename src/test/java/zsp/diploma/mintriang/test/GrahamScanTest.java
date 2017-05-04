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
                new Vector(0, 0));

        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 5), new Pair<>(5, 6), new Pair<>(6, 7), new Pair<>(7, 8), new Pair<>(8, 0));

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
