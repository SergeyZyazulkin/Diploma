package zsp.diploma.mintriang.test;

import org.junit.Assert;
import org.junit.Test;
import zsp.diploma.mintriang.algorithm.step.impl.GeneralPolygonsKruskal;
import zsp.diploma.mintriang.algorithm.step.impl.GrahamScan;
import zsp.diploma.mintriang.exception.InvalidInputPointsException;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.base.Vector;
import zsp.diploma.mintriang.model.geometry.ConvexHull;
import zsp.diploma.mintriang.model.geometry.GeneralPolygon;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;

import java.util.ArrayList;
import java.util.List;

public class GeneralPolygonsKruskalTest extends BaseTest {

    @SuppressWarnings("unchecked")
    private void testGeneralPolygonsKruskal(List<Point> points, List<List<Point>> expectedPoints, Integer... edges)
            throws TriangulationException {

        GrahamScan grahamScan = new GrahamScan();
        GeneralPolygonsKruskal generalPolygonsKruskal = new GeneralPolygonsKruskal();
        ConvexHull convexHull = grahamScan.buildConvexHull(geometryFactory, points);
        List<GeneralPolygon> generalPolygons = generalPolygonsKruskal.buildGeneralPolygons(geometryFactory, convexHull);
        List<Triangulation> triangulations = (List<Triangulation>) ((List) generalPolygons);
        visualize(geometryFactory.createTriangulation(triangulations));

        for (int i = 0; i < expectedPoints.size(); ++i) {
            Assert.assertTrue(equal(generalPolygons.get(i).getPoints(false), expectedPoints.get(i)));
            Assert.assertTrue(generalPolygons.get(i).getEdges().size() == edges[i]);
        }
    }

    @Test
    public void testTriangle() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(-1, -1), new Vector(-1, 0), new Vector(-1, 1), new Vector(-1, 2),
                new Vector(0, 1), new Vector(1, 0), new Vector(2, -1), new Vector(1, -1), new Vector(0, -1),
                new Vector(0, 0));

        List<List<Point>> pointsList = new ArrayList<List<Point>>() {{
            add(buildPoints(points, 0, 1, 2, 4, 9, 8));
            add(buildPoints(points, 8, 9, 4, 5, 7));
            add(buildPoints(points, 5, 6, 7));
            add(buildPoints(points, 2, 3, 4));
        }};

        testGeneralPolygonsKruskal(points, pointsList, 6, 5, 3, 3);
    }

    @Test
    public void testTriangle2() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(0, 0), new Vector(1, 1), new Vector(2, 2), new Vector(-2, 2),
                new Vector(-1, 1));

        List<List<Point>> pointsList = new ArrayList<List<Point>>() {{
            add(buildPoints(points, 0, 1, 2, 3, 4));
        }};

        testGeneralPolygonsKruskal(points, pointsList, 5);
    }

    @Test
    public void testTriangle3() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(-1, -1), new Vector(1, -1), new Vector(0, 1));

        List<List<Point>> pointsList = new ArrayList<List<Point>>() {{
            add(buildPoints(points, 0, 1, 2));
        }};

        testGeneralPolygonsKruskal(points, pointsList, 3);
    }

    @Test
    public void testRectangle() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(-1, -1), new Vector(-1, 0), new Vector(-1, 1), new Vector(0, 1),
                new Vector(1, 1), new Vector(1, 0), new Vector(1, -1), new Vector(0, -1), new Vector(0, 0));

        List<List<Point>> pointsList = new ArrayList<List<Point>>() {{
            add(buildPoints(points, 0, 1, 2, 3, 4, 5, 6, 7, 8));
        }};

        testGeneralPolygonsKruskal(points, pointsList, 9);
    }

    @Test
    public void testPolygon() throws TriangulationException {
        List<Point> points = buildPoints(new Vector(1, 0), new Vector(2, 0), new Vector(0, 1), new Vector(1, 0.5),
                new Vector(1.5, 0.5), new Vector(3, 0), new Vector(0, 2), new Vector(1, 2.5), new Vector(1.5, 1),
                new Vector(3, 2), new Vector(1, 3), new Vector(2, 3));

        List<List<Point>> pointsList = new ArrayList<List<Point>>() {{
            add(buildPoints(points, 0, 1, 3, 4));
            add(buildPoints(points, 1, 5, 9, 11, 10, 7, 6, 2, 3, 8, 4));
            add(buildPoints(points, 6, 7, 10));
            add(buildPoints(points, 0, 2, 3));
        }};

        testGeneralPolygonsKruskal(points, pointsList, 4, 11, 3, 3);
    }
}
