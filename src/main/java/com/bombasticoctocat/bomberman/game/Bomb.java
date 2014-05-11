package com.bombasticoctocat.bomberman.game;

import java.util.List;

public class Bomb {
    private final Tile tile;

    public Bomb(Tile tile) {
        this.tile = tile;
    }

    public List<Flames> detonate() {
        return tile.detonate();
    }

    public int range() {
        return 1;
    }
}
