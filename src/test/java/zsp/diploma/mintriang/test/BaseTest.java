package zsp.diploma.mintriang.test;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import zsp.diploma.mintriang.algorithm.TriangulationAlgorithm;
import zsp.diploma.mintriang.algorithm.UnionAlgorithm;
import zsp.diploma.mintriang.algorithm.impl.*;
import zsp.diploma.mintriang.algorithm.step.impl.*;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.base.Pair;
import zsp.diploma.mintriang.model.base.Vector;
import zsp.diploma.mintriang.model.geometry.Edge;
import zsp.diploma.mintriang.model.geometry.GeometryFactory;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;
import zsp.diploma.mintriang.model.geometry.impl.GeometryFactoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Ignore
public class BaseTest {

    private static final double LIMIT = 200;

    protected static GeometryFactory geometryFactory = new GeometryFactoryImpl();

    protected static List<Point> buildPoints(Vector... vectors) {
        List<Point> points = new ArrayList<>();

        for (Vector vector : vectors) {
            points.add(geometryFactory.createPoint(vector));
        }

        return points;
    }

    protected static List<Edge> buildEdges(List<Point> points, Pair<Integer, Integer>... pairs) {
        List<Edge> edges = new ArrayList<>();

        for (Pair<Integer, Integer> pair : pairs) {
            edges.add(geometryFactory.createEdge(points.get(pair.getValue1()), points.get(pair.getValue2()), false));
        }

        return edges;
    }

    protected static List<Point> buildPoints(List<Point> points, Integer... indexes) {
        List<Point> result = new ArrayList<>();

        for (Integer index : indexes) {
            result.add(points.get(index));
        }

        return result;
    }

    protected static void visualize(Triangulation triangulation) {
        Visualizer.visualize(triangulation);
    }

    protected static boolean equal(List l1, List l2) {
        return l1.size() == l2.size() && l1.containsAll(l2) && l2.containsAll(l1);
    }

    protected static Triangulation buildTriangulation(List<Point> points, List<Edge> edges) {
        return geometryFactory.createTriangulation(points, edges);
    }

    protected static List<Point> clone(List<Point> points) {
        return points.parallelStream().map(Point::clonePoint).collect(Collectors.toList());
    }

    protected static List<Point> getRandomPoints(int count) {
        return Stream.generate(BaseTest::getRandomVector)
                .limit(count)
                .map(geometryFactory::createPoint)
                .collect(Collectors.toList());
    }

    private static Vector getRandomVector() {
        return new Vector(getRandom(), getRandom());
    }

    private static double getRandom() {
        return (Math.random() - 0.5) * LIMIT;
    }

    public void testAll() throws TriangulationException {
        Heuristic1 heuristic1 = Heuristic1.newBuilder()
                .setGeometryFactory(geometryFactory)
                .setConvexHullStep(new GrahamScan())
                .setGeneralPolygonStep(new GeneralPolygonKruskal())
                .setGeneralPolygonTriangulationStep(new GeneralPolygonTriangulation())
                .build();

        Heuristic2 heuristic2 = Heuristic2.newBuilder()
                .setGeometryFactory(geometryFactory)
                .setConvexHullStep(new GrahamScan())
                .setGeneralPolygonsStep(new GeneralPolygonsKruskal())
                .setGeneralPolygonTriangulationStep(new GeneralPolygonTriangulation())
                .build();

        Delaunay delaunay = Delaunay.newBuilder()
                .setGeometryFactory(geometryFactory)
                .setBaseTriangulationStep(new BaseTriangulation())
                .setDelaunayConditionStep(new DelaunayCondition())
                .build();

        UnionAlgorithm union = TriangulationUnion.newBuilder()
                .setGeometryFactory(geometryFactory)
                .setDicotNetworkStep(new IntersectionGraph())
                .setUnitedTriangulationStep(new UnitedTriangulation())
                .build();

        TriangulationAlgorithm greedy = new Greedy(geometryFactory);

        List<Point> points1 = getRandomPoints(300);
        List<Point> points2 = clone(points1);
        List<Point> points3 = clone(points1);
        List<Point> points4 = clone(points1);

        Triangulation triangulation1 = heuristic1.triangulate(points1);
        Triangulation triangulation2 = heuristic2.triangulate(points2);
        Triangulation triangulation3 = greedy.triangulate(points3);
        Triangulation triangulation4 = delaunay.triangulate(points4);
        Triangulation united1 = union.unite(triangulation1, triangulation2);
        Triangulation united2 = union.unite(united1, triangulation3);
        Triangulation united3 = union.unite(united2, triangulation4);

        Visualizer.visualize(triangulation1, "heuristic1.png");
        Visualizer.visualize(triangulation2, "heuristic2.png");
        Visualizer.visualize(triangulation3, "greedy.png");
        Visualizer.visualize(triangulation4, "delaunay.png");
        Visualizer.visualize(united1, "united1.png");
        Visualizer.visualize(united2, "united2.png");
        Visualizer.visualize(united3, "united3.png");

        System.out.println(String.format(
                "Heuristic1: %f\nHeuristic2: %f\nGreedy: %f\nDelaunay: %f\nUnited1: %f\nUnited2: %f\nUnited3: %f",
                triangulation1.getLength(), triangulation2.getLength(), triangulation3.getLength(),
                triangulation4.getLength(), united1.getLength(), united2.getLength(), united3.getLength()));

        Assert.assertTrue(triangulation1.getEdges().size() == triangulation2.getEdges().size());
        Assert.assertTrue(triangulation1.getEdges().size() == triangulation3.getEdges().size());
        Assert.assertTrue(triangulation1.getEdges().size() == triangulation4.getEdges().size());
        Assert.assertTrue(triangulation1.getEdges().size() == united1.getEdges().size());
        Assert.assertTrue(triangulation1.getEdges().size() == united2.getEdges().size());
        Assert.assertTrue(triangulation1.getEdges().size() == united3.getEdges().size());
    }
}
