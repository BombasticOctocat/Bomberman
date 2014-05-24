package com.bombasticoctocat.bomberman.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BoardMap {
    private List<List<Tile>> tiles;
    private Door door;
    private Powerup powerup;

    public BoardMap(TilesFactory factory, Powerup powerup) {
        tiles = new ArrayList<>();

        for (int i = 0; i < tilesVertical(); i++) {
            List<Tile> row = new ArrayList<>();
            tiles.add(row);
            for (int j = 0; j < tilesHorizontal(); j++) {
                row.add(factory.createForCoordinates(i, j));
            }
        }
        setUpDoor();
        setUpPowerup(powerup);
    }

    private void setUpDoor() {
        Random rg = new Random();
        int x, y;
        do {
            x = rg.nextInt(tilesHorizontal());
            y = rg.nextInt(tilesVertical());
        } while (getTileAt(x, y).getType() != Tile.Type.BRICKS);
        door = new Door(getTileAt(x, y));
    }

    private void setUpPowerup(Powerup powerup) {
        Random rg = new Random();
        int x, y;
        do {
            x = rg.nextInt(tilesHorizontal());
            y = rg.nextInt(tilesVertical());
        } while (getTileAt(x, y).getType() != Tile.Type.BRICKS || getTileAt(x, y) == door.getTile());
        powerup.setTile(getTileAt(x, y));
        this.powerup = powerup;
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

        Tile t;

        t = getTileAt(column, row);
        result.add(t);
        if (t.getType() == Tile.BRICKS || t.getType() == Tile.CONCRETE) {
            return result;
        }

        for (int x = column - 1; x >= column - range; x--) {
            t = getTileAt(x, row);
            result.add(t);
            if (t.getType() == Tile.BRICKS || t.getType() == Tile.CONCRETE) {
                break;
            }
        }
        for (int x = column + 1; x <= column + range; x++) {
            t = getTileAt(x, row);
            result.add(t);
            if (t.getType() == Tile.BRICKS || t.getType() == Tile.CONCRETE) {
                break;
            }
        }

        for (int y = row - 1; y >= row - range; y--) {
            t = getTileAt(column, y);
            result.add(t);
            if (t.getType() == Tile.BRICKS || t.getType() == Tile.CONCRETE) {
                break;
            }
        }
        for (int y = row + 1; y <= row + range; y++) {
            t = getTileAt(column, y);
            result.add(t);
            if (t.getType() == Tile.BRICKS || t.getType() == Tile.CONCRETE) {
                break;
            }
        }
        return result;
    }

    public List<Tile> adjacentTiles(int col, int row) {
        List<Tile> result = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Tile tile = getTileAt(col + i, row + j);
                if (tile != null) {
                    result.add(tile);
                }
            }
        }

        return result;
    }
    public Goomba placeGoombaAtRandom(Goomba.Type type, Board board) {
        Random rg = new Random();
        while (true) {
            int col = rg.nextInt(tilesHorizontal());
            int row = rg.nextInt(tilesVertical());
            if (row > 3 && col > 3 && getTileAt(col, row).getType() == Tile.Type.EMPTY) {
                return new Goomba(board, type, row, col);
            }
        }
    }

    public Door getDoor() {
        return door;
    }

    public Powerup getPowerup() { return powerup; }
}
