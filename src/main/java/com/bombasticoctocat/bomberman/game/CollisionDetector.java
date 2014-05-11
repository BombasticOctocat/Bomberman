package com.bombasticoctocat.bomberman.game;

import java.util.ArrayList;
import java.util.List;

public class CollisionDetector {
    private final BoardMap map;

    public CollisionDetector(BoardMap map) {
        this.map = map;
    }

    public Displacement blockDisplacement(Particle particle, Displacement move) {
        double fromX1 = particle.getX();
        double fromX2 = fromX1 + particle.width();
        double fromY1 = particle.getY();
        double fromY2 = fromY1 + particle.height();

        double toX1 = fromX1 + move.getX();
        double toX2 = toX1 + particle.width();
        double toY1 = fromY1 + move.getY();
        double toY2 = toY1 + particle.height();

        List<Tile> tiles = adjacentTiles(particle);
        for (Tile tile : tiles) {
            if (fromX2 <= tile.getX() && toX2 > tile.getX() && fromY2 > tile.getY() && fromY1 < tile.getY() + tile.height()) {
                toX2 = tile.getX();
                toX1 = toX2 - particle.width();
            }

            if (fromX1 >= tile.getX() + tile.width() && toX1 < tile.getX() + tile.width() && fromY2 > tile.getY() && fromY1 < tile.getY() + tile.height()) {
                toX1 = tile.getX() + tile.width();
                toX2 = toX1 + particle.width();
            }
        }

        for (Tile tile : tiles) {
            if (fromY2 <= tile.getY() && toY2 > tile.getY() && toX2 > tile.getX() && toX1 < tile.getX() + tile.width()) {
                toY2 = tile.getY();
                toY1 = toY2 - particle.height();
            }

            if (fromY1 >= tile.getY() + tile.height() && toY1 < tile.getY() + tile.height() && toX2 > tile.getX() && toX1 < tile.getX() + tile.width()) {
                toY1 = tile.getY() + tile.height();
                toY2 = toY1 + particle.height();
            }
        }

        return new Displacement(toX1 - fromX1, toY1 - fromY1);
    }

    public List<Tile> adjacentTiles(Particle particle) {
        int col = particle.getColumn();
        int row = particle.getRow();

        List<Tile> result = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Tile tile = map.getTileAt(col+i, row+j);
                if (tile != null && particle.shouldCollideWith(tile)) {
                    result.add(tile);
                }
            }
        }

        return result;
    }
}
