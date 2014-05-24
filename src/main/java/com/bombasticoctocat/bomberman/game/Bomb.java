package com.bombasticoctocat.bomberman.game;

public class Bomb {
    private Detonator detonator;
    private Tile tile;
    private boolean isDetonated = false;

    public Bomb(Detonator detonator) {
        this.detonator = detonator;
    }

    public void detonate() {
        if ( ! isDetonated && ! detonator.hasManualDetonator()) {
            detonator.detonate(this);
        }
    }

    public boolean isDetonated() {
        return isDetonated;
    }

    public void markAsDetonated()
    {
        isDetonated = true;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }
}
