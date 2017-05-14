package zsp.diploma.mintriang.test.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import zsp.diploma.mintriang.algorithm.TriangulationAlgorithm;
import zsp.diploma.mintriang.algorithm.UnionAlgorithm;
import zsp.diploma.mintriang.algorithm.impl.*;
import zsp.diploma.mintriang.algorithm.step.impl.*;
import zsp.diploma.mintriang.exception.AlgorithmIncompleteException;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.base.Vector;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;
import zsp.diploma.mintriang.test.BaseTest;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Charts {

    private static final String DIRECTORY = "charts";
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    private static final int POINT_RADIUS = 1;

    public static void main(String[] args) throws Exception {
        double l1;
        double l2;

        do {
            List<Point> points = BaseTest.getRandomPoints(1000);
            TriangulationAlgorithm[] tas = createAlgorithms();
            Triangulation h1 = tas[0].triangulate(BaseTest.clone(points));
            Triangulation h2 = tas[1].triangulate(BaseTest.clone(points));
            Triangulation delaunay = tas[2].triangulate(BaseTest.clone(points));
            Triangulation greedy = tas[3].triangulate(BaseTest.clone(points));
            Triangulation random = tas[4].triangulate(BaseTest.clone(points));

            UnionAlgorithm union = TriangulationUnionAlgorithm.newBuilder()
                    .setGeometryFactory(BaseTest.geometryFactory)
                    .setDicotNetworkBuilder(new DicotNetworkBuilderImpl())
                    .setTriangulationCombiner(new TriangulationCombinerImpl())
                    .build();

            LocalImprovementAlgorithm localImprovementAlgorithm = LocalImprovementAlgorithm.newBuilder()
                    .setGeometryFactory(BaseTest.geometryFactory)
                    .setLocalTriangulationExtractor(new LocalTriangulationExtractorImpl())
                    .setGeneralPolygonTriangulationBuilder(new GeneralPolygonTriangulationBuilderImpl())
                    .setTriangulationUnionAlgorithm(TriangulationUnionAlgorithm.newBuilder()
                            .setGeometryFactory(BaseTest.geometryFactory)
                            .setDicotNetworkBuilder(new DicotNetworkBuilderImpl())
                            .setTriangulationCombiner(new TriangulationCombinerImpl())
                            .build())
                    .setLocalTriangulationCombiner(new LocalTriangulationCombinerImpl())
                    .build();

            Triangulation united = union.unite(h1, delaunay);
            l1 = h1.getLength();
            l2 = united.getLength();
            Triangulation improved = localImprovementAlgorithm.improveTriangulation(random);
        } while (Math.abs(l1 - l2) < 0.0001);
    }

    private static String getFileName(String name) {
        return Paths.get(DIRECTORY, name + ".png").toString();
    }

    private static void drawChart(String name, XYSeriesCollection xySeriesCollection) throws IOException {
        JFreeChart chart = ChartFactory.createXYLineChart(null, "number of points", "length of triangulation",
                xySeriesCollection, PlotOrientation.VERTICAL, true, true, false);

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        for (int i = 0; i < xySeriesCollection.getSeriesCount(); ++i) {
            renderer.setSeriesShapesVisible(i, true);
            renderer.setSeriesShapesFilled(i, true);

            renderer.setSeriesShape(i, new Ellipse2D.Double(
                    -POINT_RADIUS, -POINT_RADIUS, 2 * POINT_RADIUS, 2 * POINT_RADIUS));

            renderer.setSeriesStroke(i, new BasicStroke(3));
        }

        plot.setRenderer(renderer);
        ChartUtilities.saveChartAsPNG(new File(getFileName(name)), chart, WIDTH, HEIGHT);
    }

    private static TriangulationAlgorithm[] createAlgorithms() throws AlgorithmIncompleteException {
        TriangulationAlgorithm[] algorithms = new TriangulationAlgorithm[5];

        algorithms[0] = MTHeuristic1.newBuilder()
                .setGeometryFactory(BaseTest.geometryFactory)
                .setConvexHullBuilder(new ConvexHullBuilderImpl())
                .setConvexGeneralPolygonBuilder(new ConvexGeneralPolygonBuilderImpl())
                .setGeneralPolygonTriangulationBuilder(new GeneralPolygonTriangulationBuilderImpl())
                .build();

        algorithms[1] = MTHeuristic2.newBuilder()
                .setGeometryFactory(BaseTest.geometryFactory)
                .setConvexHullBuilder(new ConvexHullBuilderImpl())
                .setGeneralPolygonsBuilder(new GeneralPolygonsBuilderImpl())
                .setGeneralPolygonTriangulationBuilder(new GeneralPolygonTriangulationBuilderImpl())
                .build();

        algorithms[2] = DelaunayTriangulationAlgorithm.newBuilder()
                .setGeometryFactory(BaseTest.geometryFactory)
                .setBaseTriangulationBuilder(new BaseTriangulationBuilderImpl())
                .setDelaunayConditionChecker(new DelaunayConditionCheckerImpl())
                .build();

        algorithms[3] = new GreedyTriangulationAlgorithm(BaseTest.geometryFactory);
        algorithms[4] = new RandomTriangulationAlgorithm(BaseTest.geometryFactory);
        return algorithms;
    }

    private static void drawDistributionTest(String name, PointSupplier pointSupplier, Integer... pointsCount)
            throws TriangulationException, IOException {

        TriangulationAlgorithm[] algorithms = createAlgorithms();
        String[] algorithmsNames = new String[] { "Heuristic 1", "Heuristic 2", "Delaunay", "Greedy", "Random" };
        XYSeries[] xySeries = new XYSeries[algorithms.length];

        for (int i = 0; i < algorithms.length; ++i) {
            xySeries[i] = new XYSeries(algorithmsNames[i]);
        }

        for (Integer count : pointsCount) {
            List<Point>[] points = pointSupplier.generatePoints(count, algorithms.length);

            for (int i = 0; i < algorithms.length; ++i) {
                double length = algorithms[i].triangulate(points[i]).getLength();
                xySeries[i].add(count.doubleValue(), length);
            }
        }

        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        Arrays.stream(xySeries).sequential().forEach(xySeriesCollection::addSeries);
        drawChart(name, xySeriesCollection);
    }

    public static void drawUniformDistributionChart() throws TriangulationException, IOException {
        drawDistributionTest("UniformDistribution", BaseTest::getRandom,
                100, 200, 300, 400, 500);
    }

    public static void drawNormalDistributionChart() throws TriangulationException, IOException {
        drawDistributionTest("NormalDistribution", BaseTest::getNormalRandom,
                100, 200, 300, 400, 500);
    }

    @FunctionalInterface
    private interface PointSupplier {

        double generateRandom();

        default Point generatePoint() {
            return BaseTest.geometryFactory.createPoint(new Vector(generateRandom(), generateRandom()));
        }

        default List<Point> generatePoints(int count) {
            return IntStream.range(0, count)
                    .boxed()
                    .map(i -> generatePoint())
                    .collect(Collectors.toList());
        }

        @SuppressWarnings("unchecked")
        default List<Point>[] generatePoints(int count, int algs) {
            List<Point> points = generatePoints(count);
            List<Point>[] pointsClones = new List[algs];
            pointsClones[0] = points;

            for (int i = 1; i < algs; ++i) {
                pointsClones[i] = BaseTest.clone(points);
            }

            return pointsClones;
        }
    }
}
