package com.bombasticoctocat.bomberman.game;

import java.util.List;

/**
 * Created by kustosz on 04/05/14.
 */

public class Board {

    private static final int WIDTH = 1550;
    private static final int HEIGHT = 650;
    private static final int TILES_HORIZONTAL = 31;
    private static final int TILES_VERTICAL = 13;

    public int tilesHorizontal() {
        return TILES_HORIZONTAL;
    }

    public int tilesVertical() {
        return TILES_VERTICAL;
    }

    public int width() {
        return WIDTH;
    }

    public int height() {
        return HEIGHT;
    }

    public Tile getTileAt(int col, int row) {
        return null;
    }

    public Hero getHero() {
        return null;
    }

    public List<Goomba> getGoombas() {
        return null;
    }

    public void tick(long timeDelta, int arrowsPressed, boolean bombPlanted) {

    }

}
