package com.bombasticoctocat.bomberman.game;

import java.util.LinkedList;
import java.util.List;

public class Tile extends Particle {
    public enum Type {
        EMPTY, BRICKS, CONCRETE;
    }

    public static final Type EMPTY = Type.EMPTY;
    public static final Type BRICKS = Type.BRICKS;
    public static final Type CONCRETE = Type.CONCRETE;

    public static final int WIDTH = 60;
    public static final int HEIGHT = 60;

    private BoardMap boardMap;
    private Timer timer;
    private Type type;
    private int row;
    private int col;
    private Bomb bomb;
    private LinkedList<Flames> flamesList;

    public Tile(BoardMap boardMap, Timer timer, Type type, int row, int col) {
        this.boardMap = boardMap;
        this.timer = timer;
        this.type = type;
        this.row = row;
        this.col = col;
        this.flamesList = new LinkedList<>();
    }

    public Type getType() {
        return type;
    }

    public Bomb plantBomb() {
        if (bomb == null) {
            bomb = new Bomb(this);
            timer.schedule(Board.FUSE_TIME, bomb::detonate);
            return bomb;
        }

        return null;
    }

    public boolean isBombPlanted() {
        return (bomb != null);
    }

    public List<Flames> detonate() {
        LinkedList<Flames> result = new LinkedList<>();
        for (Tile tile : boardMap.tilesInRange(getColumn(), getRow(), bomb.range())) {
            Flames flames = tile.setOnFire();
            if (flames != null) {
                result.add(flames);
            }
        }
        bomb = null;
        return result;
    }

    public Flames setOnFire() {
        if (type != Tile.CONCRETE) {
            type = Tile.EMPTY;
            Flames flames = new Flames(this);
            flamesList.add(flames);
            timer.schedule(Board.FLAMES_DURATION, flames::clear);

            if (bomb != null) {
                bomb.detonate();
            }
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
