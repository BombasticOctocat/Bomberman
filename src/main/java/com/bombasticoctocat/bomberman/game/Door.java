package com.bombasticoctocat.bomberman.game;

/**
 * Created by kustosz on 18/05/14.
 */
public class Door {

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
