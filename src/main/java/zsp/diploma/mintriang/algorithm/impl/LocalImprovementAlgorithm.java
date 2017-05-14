package zsp.diploma.mintriang.algorithm.impl;

import zsp.diploma.mintriang.algorithm.step.GeneralPolygonTriangulationBuilder;
import zsp.diploma.mintriang.algorithm.step.LocalTriangulationCombiner;
import zsp.diploma.mintriang.algorithm.step.LocalTriangulationExtractor;
import zsp.diploma.mintriang.exception.AlgorithmIncompleteException;
import zsp.diploma.mintriang.exception.TriangulationException;
import zsp.diploma.mintriang.model.geometry.*;
import zsp.diploma.mintriang.util.Checker;
import zsp.diploma.mintriang.util.Geometry;
import zsp.diploma.mintriang.util.Visualizer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LocalImprovementAlgorithm implements zsp.diploma.mintriang.algorithm.LocalImprovementAlgorithm {

    private GeometryFactory geometryFactory;
    private LocalTriangulationExtractor localTriangulationExtractor;
    private GeneralPolygonTriangulationBuilder generalPolygonTriangulationBuilder;
    private TriangulationUnionAlgorithm triangulationUnionAlgorithm;
    private LocalTriangulationCombiner localTriangulationCombiner;

    private LocalImprovementAlgorithm(GeometryFactory geometryFactory,
                                      LocalTriangulationExtractor localTriangulationExtractor,
                                      GeneralPolygonTriangulationBuilder generalPolygonTriangulationBuilder,
                                      TriangulationUnionAlgorithm triangulationUnionAlgorithm,
                                      LocalTriangulationCombiner localTriangulationCombiner)
            throws AlgorithmIncompleteException {

        this.geometryFactory = geometryFactory;
        this.localTriangulationExtractor = localTriangulationExtractor;
        this.generalPolygonTriangulationBuilder = generalPolygonTriangulationBuilder;
        this.triangulationUnionAlgorithm = triangulationUnionAlgorithm;
        this.localTriangulationCombiner = localTriangulationCombiner;
        checkParameters();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    private void checkParameters() throws AlgorithmIncompleteException {
        if (!Checker.notNull(geometryFactory, localTriangulationExtractor,
                generalPolygonTriangulationBuilder, triangulationUnionAlgorithm, localTriangulationCombiner)) {

            throw new AlgorithmIncompleteException();
        }
    }

    @Override
    public Triangulation improveTriangulation(Triangulation triangulation) throws TriangulationException {
        GeneralPolygon localGeneralPolygon;
        double lengthBefore;
        double lengthAfter;
        boolean vis = true;

        do {
            triangulation.normalize();
            localTriangulationExtractor.reset();
            lengthBefore = triangulation.getLength();

            while ((localGeneralPolygon =
                    localTriangulationExtractor.extractLocalTriangulation(geometryFactory, triangulation)) != null) {

                Triangulation localTriangulation = extractLocalTriangulation(triangulation, localGeneralPolygon);

                Triangulation builtLocalTriangulation =
                        generalPolygonTriangulationBuilder.buildTriangulation(geometryFactory, localGeneralPolygon);

                Triangulation united = triangulationUnionAlgorithm.unite(localTriangulation, builtLocalTriangulation);

                triangulation = localTriangulationCombiner.mergeLocalTriangulation(
                        geometryFactory, triangulation, localTriangulation, united);
            }

            lengthAfter = triangulation.getLength();

            if (vis) {
                vis = false;
                Visualizer.visualize(triangulation, "zPartLI.png");
            }
        } while (lengthAfter < lengthBefore);

        Visualizer.visualize(triangulation, "zLI.png");
        return triangulation;
    }

    private Triangulation extractLocalTriangulation(Triangulation triangulation, GeneralPolygon localGeneralPolygon) {
        Set<Point> localPoints = new HashSet<>(localGeneralPolygon.getPoints(false));
        List<Point> boundary = localGeneralPolygon.getBoundaryPoints();
        Set<Point> boundSet = new HashSet<>(boundary);

        List<Edge> edges = triangulation.getEdges().parallelStream()
                .filter(e -> {
                    if (localPoints.contains(e.getFirstPoint()) && localPoints.contains(e.getSecondPoint())) {
                        if (boundSet.contains(e.getFirstPoint()) && boundSet.contains(e.getSecondPoint())) {
                            int ind1 = -1;
                            int ind2 = -1;

                            for (int i = 0; i < boundary.size(); ++i) {
                                if (boundary.get(i).equals(e.getFirstPoint())) {
                                    ind1 = i;
                                }

                                if (boundary.get(i).equals(e.getSecondPoint())) {
                                    ind2 = i;
                                }
                            }

                            if (next(ind1, 0, boundary.size() - 1) != ind2 &&
                                    next(ind2, 0, boundary.size() - 1) != ind1) {

                                return Geometry.isInside(boundary.get(prev(ind1, 0, boundary.size() - 1)),
                                        boundary.get(ind1), boundary.get(next(ind1, 0, boundary.size() - 1)),
                                        boundary.get(ind2), Geometry.Turn.COUNTER_CLOCKWISE) &&
                                        Geometry.isInside(boundary.get(prev(ind2, 0, boundary.size() - 1)),
                                                boundary.get(ind2), boundary.get(next(ind2, 0, boundary.size() - 1)),
                                                boundary.get(ind1), Geometry.Turn.COUNTER_CLOCKWISE);
                            } else {
                                return true;
                            }
                        } else {
                            return true;
                        }
                    } else {
                        return false;
                    }
                })
                .collect(Collectors.toList());

        return geometryFactory.createTriangulation(localGeneralPolygon.getPoints(false), edges);
    }

    private int next(int i, int min, int max) {
        return i < max ? i + 1 : min;
    }

    private int prev(int i, int min, int max) {
        return i > min ? i - 1 : max;
    }

    public static class Builder {

        private GeometryFactory geometryFactory;
        private LocalTriangulationExtractor localTriangulationExtractor;
        private GeneralPolygonTriangulationBuilder generalPolygonTriangulationBuilder;
        private TriangulationUnionAlgorithm triangulationUnionAlgorithm;
        private LocalTriangulationCombiner localTriangulationCombiner;

        private Builder() {
        }

        public Builder setGeometryFactory(GeometryFactory geometryFactory) {
            this.geometryFactory = geometryFactory;
            return this;
        }

        public Builder setLocalTriangulationExtractor(LocalTriangulationExtractor localTriangulationExtractor) {
            this.localTriangulationExtractor = localTriangulationExtractor;
            return this;
        }

        public Builder setGeneralPolygonTriangulationBuilder(
                GeneralPolygonTriangulationBuilder generalPolygonTriangulationBuilder) {

            this.generalPolygonTriangulationBuilder = generalPolygonTriangulationBuilder;
            return this;
        }

        public Builder setTriangulationUnionAlgorithm(TriangulationUnionAlgorithm triangulationUnionAlgorithm) {
            this.triangulationUnionAlgorithm = triangulationUnionAlgorithm;
            return this;
        }

        public Builder setLocalTriangulationCombiner(LocalTriangulationCombiner localTriangulationCombiner) {
            this.localTriangulationCombiner = localTriangulationCombiner;
            return this;
        }

        public LocalImprovementAlgorithm build() throws AlgorithmIncompleteException {
            return new LocalImprovementAlgorithm(geometryFactory, localTriangulationExtractor,
                    generalPolygonTriangulationBuilder, triangulationUnionAlgorithm, localTriangulationCombiner);
        }
    }
}
