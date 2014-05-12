package com.bombasticoctocat.bomberman.game;

public class DeathDetector extends AbstractDetector {
    public DeathDetector(BoardMap map) {
        super(map);
    }

    public boolean shouldDie(Particle particle) {
        double x1 = particle.getX();
        double x2 = x1 + particle.width();
        double y1 = particle.getY();
        double y2 = y1 + particle.height();

        for (Tile tile : adjacentTiles(particle)) {
            if (x2 > tile.getX() && x1 < tile.getX() + tile.width() && y2 > tile.getY() && y1 < tile.getY() + tile.height()) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected boolean shouldDetect(Particle particle, Tile tile) {
        return tile.isOnFire();
    }

}
