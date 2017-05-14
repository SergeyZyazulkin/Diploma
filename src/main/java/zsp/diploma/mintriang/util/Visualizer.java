package zsp.diploma.mintriang.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import zsp.diploma.mintriang.model.base.Vector;
import zsp.diploma.mintriang.model.geometry.Edge;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;
import zsp.diploma.mintriang.model.geometry.impl.EdgeImpl;
import zsp.diploma.mintriang.model.geometry.impl.PointImpl;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Visualizer {

    private static final String DIRECTORY = "test";
    private static final int BOUND = 1;
    private static final int MAX_SIZE = 800;
    private static final int POINT_RADIUS = 2;
    private static boolean b = true;

    public static void visualize(Triangulation triangulation) {
        visualize(triangulation, null);
    }

    public static void visualize(Triangulation triangulation, String name) {
        if (!name.equals("zTU.png") || b) {
            if (name.equals("zTU.png")) {
                b = false;
            }

            System.out.println(String.format("%s: %f", name, triangulation.getLength()));
            XYSeriesCollection xySeriesCollection = createDataSet(triangulation);
            Edge chartEdge = getChartEdge(triangulation);
            JFreeChart chart = customizeChart(chartEdge, xySeriesCollection);
            saveChart(chart, chartEdge, name);
        }
    }

    private static XYSeriesCollection createDataSet(Triangulation triangulation) {
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        List<Edge> edges = triangulation.getEdges();
        List<Point> points = triangulation.getPoints();

        for (Edge edge : edges) {
            XYSeries xySeries = new XYSeries(edge.toString());
            xySeries.add(edge.getFirstPoint().getCoordinates().getX(), edge.getFirstPoint().getCoordinates().getY());
            xySeries.add(edge.getSecondPoint().getCoordinates().getX(), edge.getSecondPoint().getCoordinates().getY());
            xySeriesCollection.addSeries(xySeries);
        }

        for (Point point : points) {
            XYSeries xySeries = new XYSeries(point.toString());
            xySeries.add(point.getCoordinates().getX(), point.getCoordinates().getY());
            xySeriesCollection.addSeries(xySeries);
        }

        return xySeriesCollection;
    }

    private static void saveChart(JFreeChart chart, Edge chartEdge, String name) {
        double xLength = chartEdge.getSecondPoint().getCoordinates().getX() -
                chartEdge.getFirstPoint().getCoordinates().getX();

        double yLength = chartEdge.getSecondPoint().getCoordinates().getY() -
                chartEdge.getFirstPoint().getCoordinates().getY();

        int width;
        int height;

        if (xLength < yLength) {
            width = (int) (xLength / yLength * MAX_SIZE);
            height = MAX_SIZE;
        } else {
            width = MAX_SIZE;
            height = (int) (yLength / xLength * MAX_SIZE);
        }

        File XYChart = new File(name != null ? name : getName());

        try {
            ChartUtilities.saveChartAsPNG(XYChart, chart, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JFreeChart customizeChart(Edge chartEdge, XYSeriesCollection xySeriesCollection) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                null, "x", "y", xySeriesCollection, PlotOrientation.VERTICAL, false, false, false);

        final XYPlot plot = chart.getXYPlot();

        plot.getDomainAxis().setRange(chartEdge.getFirstPoint().getCoordinates().getX(),
                chartEdge.getSecondPoint().getCoordinates().getX());

        plot.getRangeAxis().setRange(chartEdge.getFirstPoint().getCoordinates().getY(),
                chartEdge.getSecondPoint().getCoordinates().getY());

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        for (int i = 0; i < xySeriesCollection.getSeriesCount(); ++i) {
            renderer.setSeriesPaint(i, Color.BLACK);
            renderer.setSeriesShapesVisible(i, true);
            renderer.setSeriesShapesFilled(i, true);

            renderer.setSeriesShape(i, new Ellipse2D.Double(
                    -POINT_RADIUS, -POINT_RADIUS, 2 * POINT_RADIUS, 2 * POINT_RADIUS));

            renderer.setSeriesFillPaint(i, Color.BLACK);
            renderer.setSeriesStroke(i, new BasicStroke(1));
        }

        plot.setRenderer(renderer);
        return chart;
    }

    private static Edge getChartEdge(Triangulation triangulation) {
        List<Point> points = triangulation.getPoints();
        double minX = points.get(0).getCoordinates().getX();
        double minY = points.get(0).getCoordinates().getY();
        double maxX = minX;
        double maxY = minY;

        for (Point point : points) {
            if (point.getCoordinates().getX() < minX) {
                minX = point.getCoordinates().getX();
            }

            if (point.getCoordinates().getX() > maxX) {
                maxX = point.getCoordinates().getX();
            }

            if (point.getCoordinates().getY() < minY) {
                minY = point.getCoordinates().getY();
            }

            if (point.getCoordinates().getY() > maxY) {
                maxY = point.getCoordinates().getY();
            }
        }

        return new EdgeImpl(new PointImpl(
                new Vector(minX - BOUND, minY - BOUND)), new PointImpl(new Vector(maxX + BOUND, maxY + BOUND)));
    }

    private static String getName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String className = getShortClassName(stackTrace[6].getClassName());
        Path path = Paths.get(DIRECTORY, className);

        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String methodName = stackTrace[7].getMethodName();
        Path path2 = Paths.get(DIRECTORY, className, methodName + ".png");
        return  path2.toString();
    }

    private static String getShortClassName(String name) {
        return name.substring(name.lastIndexOf('.') + 1, name.length());
    }
}
