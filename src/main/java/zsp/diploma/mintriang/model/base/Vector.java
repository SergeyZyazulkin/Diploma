package zsp.diploma.mintriang.model.base;

import java.util.Arrays;

public class Vector {

    private double[] coords;

    public Vector(double... coords) {
        if (coords != null) {
            this.coords = coords;
        } else {
            this.coords = new double[0];
        }
    }

    public double get(int index) {
        return coords[index];
    }

    public double get() {
        return get(0);
    }

    public Vector set(int index, double value) {
        coords[index] = value;
        return this;
    }

    public Vector set(double value) {
        return set(0, value);
    }

    public double getX() {
        return get(0);
    }

    public Vector setX(double x) {
        return set(0, x);
    }

    public double getY() {
        return get(1);
    }

    public Vector setY(double y) {
        return set(1, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Vector vector = (Vector) o;

        return Arrays.equals(coords, vector.coords);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(coords);
    }
}
