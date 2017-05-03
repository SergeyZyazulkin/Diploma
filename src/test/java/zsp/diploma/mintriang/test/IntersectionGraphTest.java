package zsp.diploma.mintriang.test;

import org.junit.Test;
import zsp.diploma.mintriang.algorithm.step.impl.IntersectionGraph;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.base.Pair;
import zsp.diploma.mintriang.model.base.Vector;
import zsp.diploma.mintriang.model.geometry.DicotNetwork;
import zsp.diploma.mintriang.model.geometry.Edge;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;

import java.util.List;

public class IntersectionGraphTest extends BaseTest {

    private void testIntersectionGraph(
            Triangulation triangulation1, Triangulation triangulation2)
            throws TriangulationException {

        IntersectionGraph intersectionGraph = new IntersectionGraph();
        DicotNetwork network = intersectionGraph.buildDicotNetwork(geometryFactory, triangulation1, triangulation2);
        visualize(geometryFactory.createTriangulation(network));
    }

    @Test
    public void testCoinciding() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(0, 0), new Vector(1, 0), new Vector(1, 1), new Vector(0, 1));
        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 0));
        testIntersectionGraph(buildTriangulation(points, edges), buildTriangulation(points, edges));
    }

    @Test
    public void testIntersecting() throws TriangulationException {
        List<Point> points1 = buildPoints(new Vector(0, 0), new Vector(2, 0), new Vector(2, 1), new Vector(1, 2),
                new Vector(0, 1));

        List<Point> points2 = buildPoints(new Vector(0, 0), new Vector(2, 0), new Vector(2, 1), new Vector(1, 2),
                new Vector(0, 1));

        List<Edge> edges1 = buildEdges(points1, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 0), new Pair<>(0, 2), new Pair<>(0, 3));

        List<Edge> edges2 = buildEdges(points2, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 0), new Pair<>(1, 4), new Pair<>(1, 3));

        testIntersectionGraph(buildTriangulation(points1, edges1), buildTriangulation(points2, edges2));
    }
}
