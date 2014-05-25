package com.bombasticoctocat.bomberman.game;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Detonator {
    private BoardMap boardMap;
    private Timer timer;
    private LinkedList<Bomb> bombs;
    private int maxBombs;
    private int range;
    private boolean manualDetonator;

    public Detonator(Timer timer) {
        this.timer = timer;
        this.bombs = new LinkedList<>();
        reset();
    }

    public void setBoardMap(BoardMap map) {
        this.boardMap = map;
    }

    public int countBombs() {
        int result = 0;
        for (Bomb bomb : bombs) {
            if ( ! bomb.isDetonated()) {
                result++;
            }
        }
        return result;
    }

    public void addBomb() {
        maxBombs++;
    }

    public void increaseRange() {
        range++;
    }

    public void setManualDetonator() {
        manualDetonator = true;
    }

    public void reset() {
        maxBombs = 1;
        range = 1;
        manualDetonator = false;
        clearBombs();
    }

    public void clearBombs() {
        bombs.clear();
    }

    public void plantBomb(int column, int row) {
        if (countBombs() >= maxBombs) { return; }

        Tile tile = boardMap.getTileAt(column, row);

        if (tile.isBombPlanted()) {
            return;
        }

        Bomb bomb = new Bomb(this);
        tile.plantBomb(bomb);
        bombs.add(bomb);
        timer.schedule(Board.FUSE_TIME, bomb::detonate);
    }

    public void detonate(Bomb bomb) {
        bomb.markAsDetonated();
        bomb.getTile().getAndRemoveBomb();
        Set<Tile> affectedTiles = preDetonate(bomb);

        for (Tile tile : affectedTiles) {
            Flames flames = tile.setOnFire();
            if (flames != null) {
                timer.schedule(Board.FLAMES_DURATION, flames::clear);
            }
        }
    }

    private Set<Tile> preDetonate(Bomb bomb) {
        bombs.remove(bomb);
        HashSet<Tile> affectedTiles = new HashSet<>();

        for (Tile tile : boardMap.tilesInRange(bomb.getTile(), range)) {
            affectedTiles.add(tile);
            if (tile.isBombPlanted()) {
                Bomb tileBomb = tile.getAndRemoveBomb();
                tileBomb.markAsDetonated();
                bombs.remove(tileBomb);
                affectedTiles.addAll(preDetonate(tileBomb));
            }
        }

        return affectedTiles;
    }

    public boolean hasManualDetonator() {
        return manualDetonator;
    }

    public void detonateFirst() {
        if ( ! manualDetonator) { return; }

        for (Bomb bomb : bombs) {
            if ( ! bomb.isDetonated()) {
                detonate(bomb);
                return;
            }
        }
    }
}
