package zsp.diploma.mintriang.util;

import zsp.diploma.mintriang.model.base.DisjointDataSet;
import zsp.diploma.mintriang.model.geometry.Edge;
import zsp.diploma.mintriang.model.geometry.GeneralPolygon;
import zsp.diploma.mintriang.model.geometry.Point;

import java.awt.geom.Line2D;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Geometry {

    public static Turn calcTurn(Point p1, Point p2, Point p3) {
        double det = (p2.getCoordinates().getX() - p1.getCoordinates().getX()) *
                (p3.getCoordinates().getY() - p1.getCoordinates().getY()) -
                (p2.getCoordinates().getY() - p1.getCoordinates().getY()) *
                        (p3.getCoordinates().getX() - p1.getCoordinates().getX());

        if (det > 0) {
            return Turn.COUNTER_CLOCKWISE;
        } else if (det < 0) {
            return Turn.CLOCKWISE;
        } else {
            return Turn.COLLINEAR;
        }
    }

    public static double calcDistance(Point p1, Point p2) {
        return Math.sqrt((p1.getCoordinates().getX() - p2.getCoordinates().getX()) *
                (p1.getCoordinates().getX() - p2.getCoordinates().getX()) +
                (p1.getCoordinates().getY() - p2.getCoordinates().getY()) *
                        (p1.getCoordinates().getY() - p2.getCoordinates().getY()));
    }

    public static double calcAngle(Point centerPoint, Point targetPoint) {
        return Math.atan2(targetPoint.getCoordinates().getY() - centerPoint.getCoordinates().getY(),
                targetPoint.getCoordinates().getX() - centerPoint.getCoordinates().getX());
    }

    public static boolean isInside(Point ap1, Point ap2, Point ap3, Point checking, Turn turn) {
        Turn opposite = turn.opposite();

        if (ap1.equals(ap3)) {
            return !isInside(ap1, ap2, checking, ap2);
        } else if (calcTurn(ap1, ap2, ap3) != opposite) {
            return calcTurn(ap1, ap2, checking) == turn && calcTurn(ap3, ap2, checking) == opposite;
        } else {
            return !isInside(ap1, ap2, ap3, checking, opposite);
        }
    }

    public static boolean isInside(Point p1, Point p2, Point p3, Point p4) {
        return Line2D.ptSegDist(p1.getCoordinates().getX(), p1.getCoordinates().getY(),
                p2.getCoordinates().getX(), p2.getCoordinates().getY(),
                p3.getCoordinates().getX(), p3.getCoordinates().getY()) == 0 &&
                Line2D.ptSegDist(p1.getCoordinates().getX(), p1.getCoordinates().getY(),
                        p2.getCoordinates().getX(), p2.getCoordinates().getY(),
                        p4.getCoordinates().getX(), p4.getCoordinates().getY()) == 0 ||
                Line2D.ptSegDist(p3.getCoordinates().getX(), p3.getCoordinates().getY(),
                        p4.getCoordinates().getX(), p4.getCoordinates().getY(),
                        p1.getCoordinates().getX(), p1.getCoordinates().getY()) == 0 &&
                        Line2D.ptSegDist(p3.getCoordinates().getX(), p3.getCoordinates().getY(),
                                p4.getCoordinates().getX(), p4.getCoordinates().getY(),
                                p2.getCoordinates().getX(), p2.getCoordinates().getY()) == 0;
    }

    public static boolean isIntersect(Point p1, Point p2, Point p3, Point p4) {
        Set<Point> points = new HashSet<>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);

        switch (points.size()) {
            case 1:
                return true;

            case 2:
                return !p1.equals(p2);

            case 3:
                return isInside(p1, p2, p3, p4);

            default:
                return Line2D.linesIntersect(p1.getCoordinates().getX(), p1.getCoordinates().getY(),
                        p2.getCoordinates().getX(), p2.getCoordinates().getY(),
                        p3.getCoordinates().getX(), p3.getCoordinates().getY(),
                        p4.getCoordinates().getX(), p4.getCoordinates().getY());
        }
    }

    public static void kruskal(GeneralPolygon generalPolygon, DisjointDataSet disjointDataSet, List<Edge> edges) {
        for (Edge edge : edges) {
            int index1 = edge.getFirstPoint().getIndex();
            int index2 = edge.getSecondPoint().getIndex();

            if (disjointDataSet.isDifferent(index1, index2)) {
                disjointDataSet.merge(index1, index2);
                edge.addToPoints();
                generalPolygon.addEdge(edge);
            }
        }
    }

    public enum Turn {
        CLOCKWISE,
        COUNTER_CLOCKWISE,
        COLLINEAR;

        public Turn opposite() {
            switch (this) {
                case CLOCKWISE:
                    return COUNTER_CLOCKWISE;

                case COUNTER_CLOCKWISE:
                    return CLOCKWISE;

                default:
                    return COLLINEAR;
            }
        }
    }
}
