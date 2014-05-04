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
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
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
