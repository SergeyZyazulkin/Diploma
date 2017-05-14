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
import zsp.diploma.mintriang.test.util.Visualizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Ignore
public class BaseTest {

    private static final double LIMIT = 200;
    private static final Random random = new Random();

    public static GeometryFactory geometryFactory = new GeometryFactoryImpl();

    public static List<Point> buildPoints(Vector... vectors) {
        List<Point> points = new ArrayList<>();

        for (Vector vector : vectors) {
            points.add(geometryFactory.createPoint(vector));
        }

        return points;
    }

    public static List<Edge> buildEdges(List<Point> points, Pair<Integer, Integer>... pairs) {
        List<Edge> edges = new ArrayList<>();

        for (Pair<Integer, Integer> pair : pairs) {
            edges.add(geometryFactory.createEdge(points.get(pair.getValue1()), points.get(pair.getValue2()), false));
        }

        return edges;
    }

    public static List<Point> buildPoints(List<Point> points, Integer... indexes) {
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

    public static Triangulation buildTriangulation(List<Point> points, List<Edge> edges) {
        return geometryFactory.createTriangulation(points, edges);
    }

    public static List<Point> clone(List<Point> points) {
        return points.parallelStream().map(Point::clonePoint).collect(Collectors.toList());
    }

    public static List<Point> getRandomPoints(int count) {
        return Stream.generate(BaseTest::getRandomVector)
                .limit(count)
                .map(geometryFactory::createPoint)
                .collect(Collectors.toList());
    }

    public static Vector getRandomVector() {
        return new Vector(getRandom(), getRandom());
    }

    public static double getRandom() {
        return (Math.random() - 0.5) * LIMIT;
    }

    public static double getNormalRandom() {
        return random.nextGaussian() * 50;
    }

    public void testAll() throws TriangulationException {
        MTHeuristic1 mtHeuristic1 = MTHeuristic1.newBuilder()
                .setGeometryFactory(geometryFactory)
                .setConvexHullBuilder(new ConvexHullBuilderImpl())
                .setConvexGeneralPolygonBuilder(new ConvexGeneralPolygonBuilderImpl())
                .setGeneralPolygonTriangulationBuilder(new GeneralPolygonTriangulationBuilderImpl())
                .build();

        MTHeuristic2 mtHeuristic2 = MTHeuristic2.newBuilder()
                .setGeometryFactory(geometryFactory)
                .setConvexHullBuilder(new ConvexHullBuilderImpl())
                .setGeneralPolygonsBuilder(new GeneralPolygonsBuilderImpl())
                .setGeneralPolygonTriangulationBuilder(new GeneralPolygonTriangulationBuilderImpl())
                .build();

        DelaunayTriangulationAlgorithm delaunayTriangulationAlgorithm = DelaunayTriangulationAlgorithm.newBuilder()
                .setGeometryFactory(geometryFactory)
                .setBaseTriangulationBuilder(new BaseTriangulationBuilderImpl())
                .setDelaunayConditionChecker(new DelaunayConditionCheckerImpl())
                .build();

        UnionAlgorithm union = TriangulationUnionAlgorithm.newBuilder()
                .setGeometryFactory(geometryFactory)
                .setDicotNetworkBuilder(new DicotNetworkBuilderImpl())
                .setTriangulationCombiner(new TriangulationCombinerImpl())
                .build();

        LocalImprovementAlgorithm localImprovementAlgorithm = LocalImprovementAlgorithm.newBuilder()
                .setGeometryFactory(geometryFactory)
                .setLocalTriangulationExtractor(new LocalTriangulationExtractorImpl())
                .setGeneralPolygonTriangulationBuilder(new GeneralPolygonTriangulationBuilderImpl())
                .setTriangulationUnionAlgorithm(TriangulationUnionAlgorithm.newBuilder()
                        .setGeometryFactory(geometryFactory)
                        .setDicotNetworkBuilder(new DicotNetworkBuilderImpl())
                        .setTriangulationCombiner(new TriangulationCombinerImpl())
                        .build())
                .setLocalTriangulationCombiner(new LocalTriangulationCombinerImpl())
                .build();

        TriangulationAlgorithm greedy = new GreedyTriangulationAlgorithm(geometryFactory);
        TriangulationAlgorithm random = new RandomTriangulationAlgorithm(geometryFactory);

        List<Point> points1 = getRandomPoints(300);
        List<Point> points2 = clone(points1);
        List<Point> points3 = clone(points1);
        List<Point> points4 = clone(points1);
        List<Point> points5 = clone(points1);

        Triangulation triangulation1 = mtHeuristic1.triangulate(points1);
        Triangulation triangulation2 = mtHeuristic2.triangulate(points2);
        Triangulation triangulation3 = greedy.triangulate(points3);
        Triangulation triangulation4 = delaunayTriangulationAlgorithm.triangulate(points4);
        Triangulation triangulation5 = random.triangulate(points5);
        Triangulation united1 = union.unite(triangulation1, triangulation2);
        Triangulation united2 = union.unite(united1, triangulation3);
        Triangulation united3 = union.unite(united2, triangulation4);
        Triangulation united4 = union.unite(united3, triangulation5);

        Visualizer.visualize(triangulation1, "heuristic1.png");
        Visualizer.visualize(triangulation2, "heuristic2.png");
        Visualizer.visualize(triangulation3, "greedy.png");
        Visualizer.visualize(triangulation4, "delaunay.png");
        Visualizer.visualize(triangulation5, "random.png");
        Visualizer.visualize(united1, "united1.png");
        Visualizer.visualize(united2, "united2.png");
        Visualizer.visualize(united3, "united3.png");
        Visualizer.visualize(united4, "united4.png");

        double united4Length = united4.getLength();
        int united4Size = united4.getEdges().size();
        Triangulation improved = localImprovementAlgorithm.improveTriangulation(united4);
        Visualizer.visualize(improved, "improved.png");

        System.out.println(String.format(
                "Heuristic1: %f\nHeuristic2: %f\nGreedy: %f\nDelaunay: %f\nRandom: %f\n" +
                        "United1: %f\nUnited2: %f\nUnited3: %f\nUnited4: %f\nImproved: %f",
                triangulation1.getLength(), triangulation2.getLength(),
                triangulation3.getLength(), triangulation4.getLength(),
                triangulation5.getLength(), united1.getLength(),
                united2.getLength(), united3.getLength(),
                united4Length, improved.getLength()));

        Assert.assertTrue(triangulation1.getEdges().size() == triangulation2.getEdges().size());
        Assert.assertTrue(triangulation1.getEdges().size() == triangulation3.getEdges().size());
        Assert.assertTrue(triangulation1.getEdges().size() == triangulation4.getEdges().size());
        Assert.assertTrue(triangulation1.getEdges().size() == triangulation5.getEdges().size());
        Assert.assertTrue(triangulation1.getEdges().size() == united1.getEdges().size());
        Assert.assertTrue(triangulation1.getEdges().size() == united2.getEdges().size());
        Assert.assertTrue(triangulation1.getEdges().size() == united3.getEdges().size());
        Assert.assertTrue(triangulation1.getEdges().size() == united4Size);
        Assert.assertTrue(triangulation1.getEdges().size() == improved.getEdges().size());
    }
}
