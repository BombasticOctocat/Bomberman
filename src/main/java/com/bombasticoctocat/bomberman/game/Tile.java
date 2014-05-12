package com.bombasticoctocat.bomberman.game;

import java.util.LinkedList;

public class Tile extends Particle {
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
    private Bomb bomb;
    private LinkedList<Flames> flamesList;

    public Tile(Type type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
        this.flamesList = new LinkedList<>();
    }

    public Type getType() {
        return type;
    }

    public void plantBomb(Bomb bomb) {
        bomb.setTile(this);
        this.bomb = bomb;
    }

    public boolean isBombPlanted() {
        return (bomb != null);
    }

    public Bomb getAndRemoveBomb() {
        Bomb result = bomb;
        bomb = null;
        return result;
    }

    public Flames setOnFire() {
        if (type != Tile.CONCRETE) {
            type = Tile.EMPTY;
            Flames flames = new Flames(this);
            flamesList.add(flames);

            return flames;
        }

        return null;
    }

    public boolean isOnFire() {
        return (flamesList.size() > 0);
    }

    public void clearFlames(Flames flames) {
        flamesList.remove(flames);
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
