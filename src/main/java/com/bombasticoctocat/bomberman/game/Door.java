package com.bombasticoctocat.bomberman.game;

public class Door {

    public Tile getTile() {
        return tile;
    }

    private Tile tile;

    public Door(Tile tile) {
        this.tile = tile;
    }

    public void update(boolean shouldOpen) {
        if (tile.getType() != Tile.Type.BRICKS && tile.getType() != Tile.Type.CONCRETE) {
            tile.setType(shouldOpen ? Tile.Type.DOOR_OPEN : Tile.Type.DOOR_CLOSED);
        }
    }

}
