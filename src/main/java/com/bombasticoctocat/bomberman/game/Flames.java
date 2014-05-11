package com.bombasticoctocat.bomberman.game;

public class Flames {
    private final Tile tile;

    public Flames(Tile tile) {
        this.tile = tile;
    }

    public void clear() {
        tile.clearFlames(this);
    }
}
