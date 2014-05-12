package com.bombasticoctocat.bomberman.game;

public class Bomb {
    private Detonator detonator;
    private Tile tile;
    private boolean isDetonated = false;

    public Bomb(Detonator detonator) {
        this.detonator = detonator;
    }

    public void detonate() {
        if ( ! isDetonated) {
            detonator.detonate(this);
        }
    }

    public void markAsDetonated()
    {
        isDetonated = true;
    }

    public int range() {
        return 1;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }
}
