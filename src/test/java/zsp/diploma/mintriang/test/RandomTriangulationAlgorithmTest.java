package zsp.diploma.mintriang.test;

import org.junit.Test;
import zsp.diploma.mintriang.algorithm.TriangulationAlgorithm;
import zsp.diploma.mintriang.algorithm.impl.*;
import zsp.diploma.mintriang.algorithm.step.BaseTriangulationBuilder;
import zsp.diploma.mintriang.algorithm.step.impl.*;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.Point;
import zsp.diploma.mintriang.model.geometry.Triangulation;

import java.util.List;

public class RandomTriangulationAlgorithmTest extends BaseTest {

    private void testRandomTriangulationAlgorithm(List<Point> points) throws TriangulationException {
        RandomTriangulationAlgorithm randomTriangulationAlgorithm = new RandomTriangulationAlgorithm(geometryFactory);
        Triangulation triangulation = randomTriangulationAlgorithm.triangulate(points);
        visualize(triangulation);
    }

    @Test
    public void testRandom() throws TriangulationException {
        List<Point> points = getRandomPoints(300);
        testRandomTriangulationAlgorithm(points);
    }
}
