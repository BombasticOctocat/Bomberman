package com.bombasticoctocat.bomberman.game;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

abstract public class AbstractDetector {
    protected final BoardMap map;

    public AbstractDetector(BoardMap map) {
        this.map = map;
    }


    public List<Tile> adjacentTiles(Particle particle) {
        int col = particle.getColumn();
        int row = particle.getRow();

        return map.adjacentTiles(col, row).stream()
                .filter(tile -> shouldDetect(particle, tile))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    abstract protected boolean shouldDetect(Particle particle, Tile tile);
}
