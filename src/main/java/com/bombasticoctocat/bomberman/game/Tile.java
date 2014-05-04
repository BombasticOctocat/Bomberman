package com.bombasticoctocat.bomberman.game;

/**
 * Created by kustosz on 04/05/14.
 */
public class Tile implements Particle {
    public enum Type {
        EMPTY, BRICKS, CONCRETE;
    }

    public Type getType() {
        return null;
    }

    @Override
    public double getX() {
        return 0;
    }

    @Override
    public double getY() {
        return 0;
    }

    @Override
    public int width() {
        return 0;
    }

    @Override
    public int height() {
        return 0;
    }
}
