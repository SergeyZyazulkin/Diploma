package zsp.diploma.mintriang.test;

import org.junit.Assert;
import org.junit.Test;
import zsp.diploma.mintriang.algorithm.TriangulationAlgorithm;
import zsp.diploma.mintriang.algorithm.impl.DelaunayTriangulationAlgorithm;
import zsp.diploma.mintriang.algorithm.step.impl.BaseTriangulationBuilderImpl;
import zsp.diploma.mintriang.algorithm.step.impl.DelaunayConditionCheckerImpl;
import zsp.diploma.mintriang.exception.InvalidInputPointsException;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.base.Pair;
import zsp.diploma.mintriang.model.base.Vector;
import zsp.diploma.mintriang.model.geometry.Edge;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;

import java.util.List;

public class DelaunayTriangulationAlgorithmTest extends BaseTest {

    private void testDelaunay(
            List<Point> points, List<Edge> expectedEdges) throws TriangulationException {

        TriangulationAlgorithm delaunay = DelaunayTriangulationAlgorithm.newBuilder()
                .setGeometryFactory(geometryFactory)
                .setBaseTriangulationBuilder(new BaseTriangulationBuilderImpl())
                .setDelaunayConditionChecker(new DelaunayConditionCheckerImpl())
                .build();

        Triangulation triangulation = delaunay.triangulate(points);
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
                new Pair<>(8, 10), new Pair<>(0, 10), new Pair<>(5, 8), new Pair<>(5, 7), new Pair<>(1, 10),
                new Pair<>(5, 10), new Pair<>(1, 4), new Pair<>(4, 10), new Pair<>(2, 4));

        testDelaunay(points, edges);
    }

    @Test
    public void testTriangle2() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(0, 0), new Vector(1, 1), new Vector(2, 2), new Vector(-2, 2),
                new Vector(-1, 1));

        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 0), new Pair<>(1, 4), new Pair<>(1, 3));

        testDelaunay(points, edges);
    }

    @Test
    public void testTriangle3() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(-1, -1), new Vector(1, -1), new Vector(0, 1));
        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 0));
        testDelaunay(points, edges);
    }

    @Test
    public void testRectangle() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(-1, -1), new Vector(-1, 0), new Vector(-1, 1), new Vector(0, 1),
                new Vector(1, 1), new Vector(1, 0), new Vector(1, -1), new Vector(0, -1), new Vector(0, 0));

        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 5), new Pair<>(5, 6), new Pair<>(6, 7), new Pair<>(7, 0), new Pair<>(7, 8),
                new Pair<>(0, 8), new Pair<>(5, 7), new Pair<>(1, 8), new Pair<>(5, 8), new Pair<>(1, 3),
                new Pair<>(3, 8), new Pair<>(4, 8));

        testDelaunay(points, edges);
    }

    @Test(expected = InvalidInputPointsException.class)
    public void testNotEnoughPoints() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(-1, -1), new Vector(-1, 0));
        testDelaunay(points, null);
    }

    @Test(expected = InvalidInputPointsException.class)
    public void testLine() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(1, -1), new Vector(1, 0), new Vector(1, 1), new Vector(1, 2),
                new Vector(1, -1), new Vector(1, 10), new Vector(1, -100), new Vector(1, 1000), new Vector(1, -1000));

        testDelaunay(points, null);
    }

    @Test(expected = InvalidInputPointsException.class)
    public void testLine2() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(1.12736782, -1.1231), new Vector(1.12736782, 0.12312),
                new Vector(1.12736782, 1.6345), new Vector(1.12736782, 2.32432), new Vector(1.12736782, -1.87567),
                new Vector(1.12736782, 10.6575), new Vector(1.12736782, -100.56756), new Vector(1.12736782, 1000.123),
                new Vector(1.12736782, -1000.7546));

        testDelaunay(points, null);
    }

    @Test
    public void testRandom() throws TriangulationException {
        List<Point> points = getRandomPoints(300);
        testDelaunay(points, null);
    }
}
