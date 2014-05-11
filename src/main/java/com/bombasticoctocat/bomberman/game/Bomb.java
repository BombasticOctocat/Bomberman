package com.bombasticoctocat.bomberman.game;

public class Bomb {
    private final Tile tile;

    public Bomb(Tile tile) {
        this.tile = tile;
    }

    public void detonate() {
        tile.detonate();
    }
}
