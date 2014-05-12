package com.bombasticoctocat.bomberman.game;

public class Displacement {
    private static final double TOLERANCE = 1e-7;
    private final double x;
    private final double y;

    public Displacement(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean equals(Displacement that) {
        return Math.abs(this.getX() - that.getX()) < TOLERANCE && Math.abs(this.getY() - that.getY()) < TOLERANCE;
    }
}
