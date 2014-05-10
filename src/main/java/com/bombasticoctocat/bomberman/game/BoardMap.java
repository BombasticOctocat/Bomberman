package com.bombasticoctocat.bomberman.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcin on 05/05/14.
 */
public class BoardMap {
    private static final int TILES_HORIZONTAL = 31;
    private static final int TILES_VERTICAL = 13;
    private static final double DENSITY = 0.3;

    private List<List<Tile>> tiles;

    public BoardMap() {
        tiles = new ArrayList<>();
        TilesFactory factory = new TilesFactory(tilesVertical(), tilesHorizontal(), density());

        for (int i = 0; i < tilesVertical(); i++) {
            List<Tile> row = new ArrayList<>();
            tiles.add(row);
            for (int j = 0; j < tilesHorizontal(); j++) {
                row.add(factory.createForCoordinates(i, j));
            }
        }
    }

    private double density() {
        return DENSITY;
    }

    public int tilesHorizontal() {
        return TILES_HORIZONTAL;
    }

    public int tilesVertical() {
        return TILES_VERTICAL;
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
}
