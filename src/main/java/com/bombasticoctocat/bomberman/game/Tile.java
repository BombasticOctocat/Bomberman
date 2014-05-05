package com.bombasticoctocat.bomberman.game;

/**
 * Created by kustosz on 04/05/14.
 */
public class Tile implements Particle {
    public enum Type {
        EMPTY, BRICKS, CONCRETE;
    }

    public static final Type EMPTY = Type.EMPTY;
    public static final Type BRICKS = Type.BRICKS;
    public static final Type CONCRETE = Type.CONCRETE;

    public static final int WIDTH = 60;
    public static final int HEIGHT = 60;

    private Type type;
    private int row;
    private int col;

    public Tile(Type type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
    }

    public Type getType() {
        return type;
    }

    @Override
    public double getX() {
        return col * width();
    }

    @Override
    public double getY() {
        return row * height();
    }

    @Override
    public int width() {
        return WIDTH;
    }

    @Override
    public int height() {
        return HEIGHT;
    }
}
