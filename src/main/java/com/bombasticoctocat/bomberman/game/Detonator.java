package com.bombasticoctocat.bomberman.game;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Detonator {
    private BoardMap boardMap;
    private Timer timer;
    private LinkedList<Bomb> bombs;

    public Detonator(BoardMap boardMap, Timer timer) {
        this.boardMap = boardMap;
        this.timer = timer;
        this.bombs = new LinkedList<>();
    }

    public void plantBomb(int column, int row) {
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

        for (Tile tile : boardMap.tilesInRange(bomb.getTile(), bomb.range())) {
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
}
