package zsp.diploma.mintriang.test;

import org.junit.Assert;
import org.junit.Test;
import zsp.diploma.mintriang.algorithm.TriangulationAlgorithm;
import zsp.diploma.mintriang.algorithm.impl.*;
import zsp.diploma.mintriang.algorithm.step.BaseTriangulationBuilder;
import zsp.diploma.mintriang.algorithm.step.impl.*;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;

import java.util.List;

public class LocalImprovementAlgorithmTest extends BaseTest {

    private void testLocalImprovementAlgorithm(Triangulation triangulation) throws TriangulationException {
        LocalImprovementAlgorithm localImprovementAlgorithm = LocalImprovementAlgorithm.newBuilder()
                .setGeometryFactory(geometryFactory)
                .setLocalTriangulationExtractor(new LocalTriangulationExtractorImpl())
                .setGeneralPolygonTriangulationBuilder(new GeneralPolygonTriangulationBuilderImpl())
                .setTriangulationUnionAlgorithm(TriangulationUnionAlgorithm.newBuilder()
                        .setTriangulationCombiner(new TriangulationCombinerImpl())
                        .setDicotNetworkBuilder(new DicotNetworkBuilderImpl())
                        .setGeometryFactory(geometryFactory)
                        .build())
                .setLocalTriangulationCombiner(new LocalTriangulationCombinerImpl())
                .build();

        double baseLength = triangulation.getLength();
        Triangulation improved = localImprovementAlgorithm.improveTriangulation(triangulation);
        visualize(improved);
        double improvedLength = improved.getLength();
        System.out.println(String.format("Base: %f\nImproved: %f\n", baseLength, improvedLength));
        Assert.assertTrue(triangulation.getEdges().size() == improved.getEdges().size());
        Assert.assertTrue(improvedLength <= baseLength + 0.0000001);
    }

    @Test
    public void testRandomHeuristic1() throws TriangulationException {
        TriangulationAlgorithm alg = MTHeuristic1.newBuilder()
                .setGeometryFactory(geometryFactory)
                .setConvexHullBuilder(new ConvexHullBuilderImpl())
                .setConvexGeneralPolygonBuilder(new ConvexGeneralPolygonBuilderImpl())
                .setGeneralPolygonTriangulationBuilder(new GeneralPolygonTriangulationBuilderImpl())
                .build();

        List<Point> points = getRandomPoints(300);
        testLocalImprovementAlgorithm(alg.triangulate(points));
    }

    @Test
    public void testRandomHeuristic2() throws TriangulationException {
        TriangulationAlgorithm alg = MTHeuristic2.newBuilder()
                .setGeometryFactory(geometryFactory)
                .setConvexHullBuilder(new ConvexHullBuilderImpl())
                .setGeneralPolygonsBuilder(new GeneralPolygonsBuilderImpl())
                .setGeneralPolygonTriangulationBuilder(new GeneralPolygonTriangulationBuilderImpl())
                .build();

        List<Point> points = getRandomPoints(300);
        testLocalImprovementAlgorithm(alg.triangulate(points));
    }

    @Test
    public void testRandomGreedy() throws TriangulationException {
        TriangulationAlgorithm alg = new GreedyTriangulationAlgorithm(geometryFactory);
        List<Point> points = getRandomPoints(300);
        testLocalImprovementAlgorithm(alg.triangulate(points));
    }

    @Test
    public void testRandomDelaunay() throws TriangulationException {
        TriangulationAlgorithm alg = DelaunayTriangulationAlgorithm.newBuilder()
                .setGeometryFactory(geometryFactory)
                .setBaseTriangulationBuilder(new BaseTriangulationBuilderImpl())
                .setDelaunayConditionChecker(new DelaunayConditionCheckerImpl())
                .build();

        List<Point> points = getRandomPoints(300);
        testLocalImprovementAlgorithm(alg.triangulate(points));
    }

    @Test
    public void testRandom() throws TriangulationException {
        BaseTriangulationBuilder baseTriangulationBuilder = new BaseTriangulationBuilderImpl();
        List<Point> points = getRandomPoints(300);
        testLocalImprovementAlgorithm(baseTriangulationBuilder.buildBaseTriangulation(geometryFactory, points));
    }
}
