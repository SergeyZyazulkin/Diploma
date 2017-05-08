package zsp.diploma.mintriang.test;

import org.junit.Assert;
import org.junit.Test;
import zsp.diploma.mintriang.algorithm.step.impl.DelaunayConditionCheckerImpl;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.base.Pair;
import zsp.diploma.mintriang.model.base.Vector;
import zsp.diploma.mintriang.model.geometry.Edge;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;

import java.util.List;

public class DelaunayConditionCheckerTest extends BaseTest {

    private void testDelaunay(
            Triangulation triangulation, List<Edge> expectedEdges) throws TriangulationException {

        DelaunayConditionCheckerImpl delaunayConditionCheckerImpl = new DelaunayConditionCheckerImpl();
        Triangulation result = delaunayConditionCheckerImpl.improveTriangulation(geometryFactory, triangulation);
        visualize(result);

        if (expectedEdges != null) {
            Assert.assertTrue(equal(result.getEdges(), expectedEdges));
        }
    }

    @Test
    public void testFlip() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(0, 0), new Vector(5, -1), new Vector(10, 0), new Vector(5, 1));

        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 0),
                new Pair<>(0, 2));

        for (Edge e : edges) {
            e.addToPoints();
        }

        List<Edge> expected = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 0),
                new Pair<>(1, 3));

        testDelaunay(buildTriangulation(points, edges), expected);
    }

    @Test
    public void testNoFlip() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(0, 0), new Vector(5, -1), new Vector(10, 0), new Vector(5, 1));

        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 0),
                new Pair<>(1, 3));

        for (Edge e : edges) {
            e.addToPoints();
        }

        List<Edge> expected = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 0),
                new Pair<>(1, 3));

        testDelaunay(buildTriangulation(points, edges), expected);
    }

    @Test
    public void testNoFlip2() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(0, 0), new Vector(-5, 1), new Vector(10, 0), new Vector(-5, -1));

        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 0),
                new Pair<>(0, 2));

        for (Edge e : edges) {
            e.addToPoints();
        }

        List<Edge> expected = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 0),
                new Pair<>(0, 2));

        testDelaunay(buildTriangulation(points, edges), expected);
    }
}
