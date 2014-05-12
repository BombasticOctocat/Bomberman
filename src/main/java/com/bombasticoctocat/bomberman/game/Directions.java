package com.bombasticoctocat.bomberman.game;

import java.util.EnumSet;

public class Directions {
    public enum Direction { UP, DOWN, LEFT, RIGHT };
    public static final Direction UP = Direction.UP;
    public static final Direction DOWN = Direction.DOWN;
    public static final Direction LEFT = Direction.LEFT;
    public static final Direction RIGHT = Direction.RIGHT;

    private EnumSet<Direction> set;

    public Directions(EnumSet<Direction> set) {
        this.set = EnumSet.copyOf(set);
    }

    public Directions() {
        this(EnumSet.noneOf(Direction.class));
    }

    private int valueOf(Direction dir) {
        return set.contains(dir) ? 1 : 0;
    }

    public int getVerticalDirection() {
        return valueOf(DOWN) - valueOf(UP);
    }

    public int getHorizontalDirection() {
        return valueOf(RIGHT) - valueOf(LEFT);
    }
}
