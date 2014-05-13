package com.bombasticoctocat.bomberman.game;

public abstract class Particle {
    public abstract double getX();
    public abstract double getY();
    public abstract int width();
    public abstract int height();

    public double getCenterX() {
        return getX() + (width() / 2);
    }

    public double getCenterY() {
        return getY() + (height() / 2);
    }

    public int getColumn() {
        double x = getX() + (width() / 2);
        return (int) (x / Tile.WIDTH);
    }

    public int getRow() {
        double y = getY() + (height() / 2);
        return (int) (y / Tile.HEIGHT);
    }

    public boolean shouldCollideWith(Tile tile) {
        return (tile.getType() != Tile.EMPTY || tile.isBombPlanted());
    }
}
