package com.bombasticoctocat.bomberman.game;

import java.util.LinkedList;
import java.util.List;

public class Bomb {
    private final Tile tile;
    private boolean detonated = false;

    public Bomb(Tile tile) {
        this.tile = tile;
    }

    public List<Flames> detonate() {
        if (detonated) {
            return new LinkedList<>();
        }

        detonated = true;
        return tile.detonate();
    }

    public int range() {
        return 1;
    }
}
