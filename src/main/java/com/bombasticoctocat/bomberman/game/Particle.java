package com.bombasticoctocat.bomberman.game;

public abstract class Particle {
    public abstract double getX();
    public abstract double getY();
    public abstract int width();
    public abstract int height();

    public int getColumn() {
        double x = getX() + (width() / 2);
        return (int) (x / Tile.WIDTH);
    }

    public int getRow() {
        double y = getY() + (height() / 2);
        return (int) (y / Tile.HEIGHT);
    }
}
