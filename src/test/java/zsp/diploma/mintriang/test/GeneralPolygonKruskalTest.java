package zsp.diploma.mintriang.test;

import org.junit.Assert;
import org.junit.Test;
import zsp.diploma.mintriang.algorithm.TriangulationAlgorithm;
import zsp.diploma.mintriang.algorithm.step.impl.GrahamScan;
import zsp.diploma.mintriang.algorithm.step.impl.GeneralPolygonKruskal;
import zsp.diploma.mintriang.exception.InvalidInputPointsException;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.base.Pair;
import zsp.diploma.mintriang.model.base.Vector;
import zsp.diploma.mintriang.model.geometry.*;

import java.util.List;

public class GeneralPolygonKruskalTest extends BaseTest {

    private void testGeneralPolygonKruskal(List<Point> points, List<Edge> expectedEdges) throws TriangulationException {
        GrahamScan grahamScan = new GrahamScan();
        GeneralPolygonKruskal generalPolygonKruskal = new GeneralPolygonKruskal();
        ConvexHull convexHull = grahamScan.buildConvexHull(geometryFactory, points);
        GeneralPolygon generalPolygon = generalPolygonKruskal.buildGeneralPolygon(geometryFactory, convexHull);
        visualize((Triangulation) generalPolygon);
        Assert.assertTrue(equal(generalPolygon.getEdges(), expectedEdges));
    }

    @Test
    public void testTriangle() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(-1, -1), new Vector(-1, 0), new Vector(-1, 1), new Vector(-1, 2),
                new Vector(0, 1), new Vector(1, 0), new Vector(2, -1), new Vector(1, -1), new Vector(0, -1),
                new Vector(0, 0));

        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 5), new Pair<>(5, 6), new Pair<>(6, 7), new Pair<>(7, 8), new Pair<>(8, 0),
                new Pair<>(8, 9));

        testGeneralPolygonKruskal(points, edges);
    }

    @Test
    public void testTriangle2() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(0, 0), new Vector(1, 1), new Vector(2, 2), new Vector(-2, 2),
                new Vector(-1, 1));

        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 0));

        testGeneralPolygonKruskal(points, edges);
    }

    @Test
    public void testTriangle3() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(-1, -1), new Vector(1, -1), new Vector(0, 1));
        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 0));
        testGeneralPolygonKruskal(points, edges);
    }

    @Test
    public void testRectangle() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(-1, -1), new Vector(-1, 0), new Vector(-1, 1), new Vector(0, 1),
                new Vector(1, 1), new Vector(1, 0), new Vector(1, -1), new Vector(0, -1), new Vector(0, 0));

        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 5), new Pair<>(5, 6), new Pair<>(6, 7), new Pair<>(7, 0), new Pair<>(7, 8));

        testGeneralPolygonKruskal(points, edges);
    }

    @Test
    public void testPolygon() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(1, 0), new Vector(2, 0), new Vector(0, 1), new Vector(1, 0.5),
                new Vector(1.5, 0.5), new Vector(3, 0), new Vector(0, 2), new Vector(1, 2.5), new Vector(1.5, 1),
                new Vector(3, 2), new Vector(1, 3), new Vector(2, 3));

        List<Edge> edges = buildEdges(points, new Pair<>(2, 6), new Pair<>(5, 9), new Pair<>(10, 11), new Pair<>(0, 1),
                new Pair<>(0, 2), new Pair<>(6, 10), new Pair<>(9, 11), new Pair<>(1, 5), new Pair<>(0, 3),
                new Pair<>(3, 4), new Pair<>(4, 8), new Pair<>(7, 10));

        testGeneralPolygonKruskal(points, edges);
    }
}
