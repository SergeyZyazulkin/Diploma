package zsp.diploma.mintriang.test;

import org.junit.Assert;
import org.junit.Test;
import zsp.diploma.mintriang.algorithm.TriangulationAlgorithm;
import zsp.diploma.mintriang.algorithm.UnionAlgorithm;
import zsp.diploma.mintriang.algorithm.impl.GreedyTriangulationAlgorithm;
import zsp.diploma.mintriang.algorithm.impl.MTHeuristic1;
import zsp.diploma.mintriang.algorithm.impl.MTHeuristic2;
import zsp.diploma.mintriang.algorithm.impl.TriangulationUnionAlgorithm;
import zsp.diploma.mintriang.algorithm.step.impl.*;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.base.Pair;
import zsp.diploma.mintriang.model.base.Vector;
import zsp.diploma.mintriang.model.geometry.Edge;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;

import java.util.List;

public class UnionAlgorithmTest extends BaseTest {

    private void testTriangulationUnion(
            Triangulation triangulation1, Triangulation triangulation2)
            throws TriangulationException {

        UnionAlgorithm unionAlgorithm = TriangulationUnionAlgorithm.newBuilder()
                .setGeometryFactory(geometryFactory)
                .setDicotNetworkBuilder(new DicotNetworkBuilderImpl())
                .setTriangulationCombiner(new TriangulationCombinerImpl())
                .build();

        Triangulation result = unionAlgorithm.unite(triangulation1, triangulation2);
        visualize(result);

        double length1 = triangulation1.getLength();
        double length2 = triangulation2.getLength();
        double resultLength = result.getLength();
        System.out.println(String.format("T1: %f\nT2: %f\nRes: %f\n", length1, length2, resultLength));
        Assert.assertTrue(triangulation1.getEdges().size() == result.getEdges().size());
        Assert.assertTrue(triangulation2.getEdges().size() == result.getEdges().size());
        Assert.assertTrue(resultLength <= length1 + 0.0000001 && resultLength <= length2 + 0.0000001);
    }

    @Test
    public void testCoinciding() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(0, 0), new Vector(1, 0), new Vector(1, 1), new Vector(0, 1));
        List<Edge> edges = buildEdges(points, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 0));
        testTriangulationUnion(buildTriangulation(points, edges), buildTriangulation(points, edges));
    }

    @Test
    public void testIntersecting() throws TriangulationException {
        List<Point> points1 = buildPoints(new Vector(0, 0), new Vector(2, 0), new Vector(2, 1), new Vector(1, 2),
                new Vector(0, 1));

        List<Point> points2 = clone(points1);

        List<Edge> edges1 = buildEdges(points1, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 0), new Pair<>(0, 2), new Pair<>(0, 3));

        List<Edge> edges2 = buildEdges(points2, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 0), new Pair<>(1, 4), new Pair<>(1, 3));

        testTriangulationUnion(buildTriangulation(points1, edges1), buildTriangulation(points2, edges2));
    }

    @Test
    public void testImprovement() throws TriangulationException {
        List<Point> points1 = buildPoints(new Vector(0, 0), new Vector(2, 0), new Vector(2, 2), new Vector(1, 2),
                new Vector(0, 2));

        List<Point> points2 = clone(points1);

        List<Edge> edges1 = buildEdges(points1, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 0), new Pair<>(0, 2), new Pair<>(0, 3));

        List<Edge> edges2 = buildEdges(points2, new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
                new Pair<>(4, 0), new Pair<>(1, 3), new Pair<>(1, 4));

        testTriangulationUnion(buildTriangulation(points1, edges1), buildTriangulation(points2, edges2));
    }

    @Test
    public void testRandom() throws TriangulationException {
        TriangulationAlgorithm alg1 = MTHeuristic1.newBuilder()
                .setGeometryFactory(geometryFactory)
                .setConvexHullBuilder(new ConvexHullBuilderImpl())
                .setConvexGeneralPolygonBuilder(new ConvexGeneralPolygonBuilderImpl())
                .setGeneralPolygonTriangulationBuilder(new GeneralPolygonTriangulationBuilderImpl())
                .build();

        TriangulationAlgorithm alg2 = new GreedyTriangulationAlgorithm(geometryFactory);

        List<Point> points1 = getRandomPoints(300);
        List<Point> points2 = clone(points1);
        Triangulation triangulation1 = alg1.triangulate(points1);
        Triangulation triangulation2 = alg2.triangulate(points2);
        testTriangulationUnion(triangulation1, triangulation2);
    }
}
