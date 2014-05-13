package com.bombasticoctocat.bomberman.game;

import java.util.EnumSet;

public class Directions {
    public enum Direction {
        UP, DOWN, LEFT, RIGHT;

        static {
            UP.left = LEFT;
            UP.right = RIGHT;
            DOWN.left = RIGHT;
            DOWN.right = LEFT;
            LEFT.left = DOWN;
            LEFT.right = UP;
            RIGHT.left = UP;
            RIGHT.right = DOWN;
        }

        private Direction left;
        private Direction right;

        public Direction turnLeft() {
            return left;
        }

        public Direction turnRight() {
            return right;
        }

    }
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

    public Directions reversed() {
        return new Directions(EnumSet.complementOf(set));
    }

    public Directions turnedLeft() {
        EnumSet<Direction> dirs = EnumSet.noneOf(Direction.class);
        for (Direction d : set) {
            dirs.add(d.turnLeft());
        }
        return new Directions(dirs);
    }

    public Directions turnedRight() {
        EnumSet<Direction> dirs = EnumSet.noneOf(Direction.class);
        for (Direction d : set) {
            dirs.add(d.turnRight());
        }
        return new Directions(dirs);
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
