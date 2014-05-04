package com.bombasticoctocat.bomberman.game;

/**
 * Created by kustosz on 04/05/14.
 */

public class Directions {
    public static final int UP = 1;
    public static final int DOWN = 1 << 1;
    public static final int LEFT = 1 << 2;
    public static final int RIGHT = 1 << 3;

    private int mask;

    public Directions(int mask) {
        this.mask = mask;
    }

    public Directions() {
        this(0);
    }

    public int getVerticalDirection() {
        return Integer.signum(mask & DOWN) - Integer.signum(mask & UP);
    }

    public int getHorizontalDirection() {
        return Integer.signum(mask & RIGHT) - Integer.signum(mask & LEFT);
    }
}
