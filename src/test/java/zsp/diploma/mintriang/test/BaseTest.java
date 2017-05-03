package zsp.diploma.mintriang.test;

import org.junit.Ignore;
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
}
