package com.bombasticoctocat.bomberman.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BoardMap {
    private List<List<Tile>> tiles;

    public BoardMap(TilesFactory factory) {
        tiles = new ArrayList<>();

        for (int i = 0; i < tilesVertical(); i++) {
            List<Tile> row = new ArrayList<>();
            tiles.add(row);
            for (int j = 0; j < tilesHorizontal(); j++) {
                row.add(factory.createForCoordinates(i, j));
            }
        }
    }

    public int tilesHorizontal() {
        return Board.TILES_HORIZONTAL;
    }

    public int tilesVertical() {
        return Board.TILES_VERTICAL;
    }

    public int width() {
        return tilesHorizontal() * Tile.WIDTH;
    }

    public int height() {
        return tilesVertical() * Tile.HEIGHT;
    }

    public Tile getTileAt(int col, int row) {
        Tile result = null;
        if (col >= 0 && col < tilesHorizontal() && row >= 0 && row < tilesVertical()) {
            result = tiles.get(row).get(col);
        }
        return result;
    }

    public List<Tile> tilesInRange(Tile tile, int range) {
        int column = tile.getColumn();
        int row = tile.getRow();

        LinkedList<Tile> result = new LinkedList<>();

        for (int x = column - range; x <= column + range; x++) {
            result.add(getTileAt(x, row));
        }
        for (int y = row - range; y <= row + range; y++) {
            result.add(getTileAt(column, y));
        }
        return result;
    }
}
