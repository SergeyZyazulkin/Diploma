package zsp.diploma.mintriang.test;

import org.junit.Assert;
import org.junit.Test;
import zsp.diploma.mintriang.algorithm.TriangulationAlgorithm;
import zsp.diploma.mintriang.algorithm.UnionAlgorithm;
import zsp.diploma.mintriang.algorithm.impl.Heuristic1;
import zsp.diploma.mintriang.algorithm.impl.Heuristic2;
import zsp.diploma.mintriang.algorithm.impl.TriangulationUnion;
import zsp.diploma.mintriang.algorithm.step.impl.*;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.base.Pair;
import zsp.diploma.mintriang.model.base.Vector;
import zsp.diploma.mintriang.model.geometry.Edge;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;

import java.util.List;
import java.util.stream.Collectors;

public class TriangulationUnionTest extends BaseTest {

    private void testTriangulationUnion(
            Triangulation triangulation1, Triangulation triangulation2)
            throws TriangulationException {

        UnionAlgorithm unionAlgorithm = TriangulationUnion.newBuilder()
                .setGeometryFactory(geometryFactory)
                .setDicotNetworkStep(new IntersectionGraph())
                .setUnitedTriangulationStep(new UnitedTriangulation())
                .build();

        Triangulation result = unionAlgorithm.unite(triangulation1, triangulation2);
        visualize(result);

        double length1 = triangulation1.getLength();
        double length2 = triangulation2.getLength();
        double resultLength = result.getLength();
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
        TriangulationAlgorithm alg1 = Heuristic1.newBuilder()
                .setGeometryFactory(geometryFactory)
                .setConvexHullStep(new GrahamScan())
                .setGeneralPolygonStep(new GeneralPolygonKruskal())
                .setGeneralPolygonTriangulationStep(new GeneralPolygonTriangulation())
                .build();

        TriangulationAlgorithm alg2 = Heuristic2.newBuilder()
                .setGeometryFactory(geometryFactory)
                .setConvexHullStep(new GrahamScan())
                .setGeneralPolygonsStep(new GeneralPolygonsKruskal())
                .setGeneralPolygonTriangulationStep(new GeneralPolygonTriangulation())
                .build();

        List<Point> points1 = getRandomPoints(300);
        List<Point> points2 = clone(points1);
        Triangulation triangulation1 = alg1.triangulate(points1);
        Triangulation triangulation2 = alg2.triangulate(points2);
        testTriangulationUnion(triangulation1, triangulation2);
    }
}
