package com.bombasticoctocat.bomberman.game;

import java.util.EnumSet;

public class Goomba extends Particle {
    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;
    private static final int TRACK_WIDTH = 2;

    public enum Type {
        LEVEL0(0.2, 0.5, 0.1);

        private double speed;
        private double turnProbability;
        private double turnBackProbability;
        private Type(double speed, double turnProbability, double turnBackProbability) {
            this.turnProbability = turnProbability;
            this.speed = speed;
            this.turnBackProbability = turnBackProbability;
        }

        public double getTurnBackProbability() {
            return turnBackProbability;
        }

        public double getTurnProbability() {
            return turnProbability;
        }

        public double getSpeed() {
            return speed;
        }
    }

    private Directions directions;
    private Type type;
    private double x;
    private double y;

    public Goomba(Type level, int row, int column) {
        this.type = level;
        this.x = column * Tile.WIDTH + (Tile.WIDTH - width()) / 2;
        this.y = row * Tile.HEIGHT + (Tile.HEIGHT - height()) / 2;
        this.directions = new Directions(EnumSet.of(Directions.Direction.RIGHT));

    }

    public void move(long timeDelta, CollisionDetector collisionDetector) {
        if (isInTileCenter()) {
            randomTurn(collisionDetector);
        }

        Displacement displacement = new Displacement(
                this.directions.getHorizontalDirection() * getSpeed() * timeDelta,
                this.directions.getVerticalDirection() * getSpeed() * timeDelta
        );

        Displacement corrected = collisionDetector.blockDisplacement(this, displacement);
        if (!corrected.equals(displacement)) {
            turnBack(collisionDetector, true);
        }

        this.x += corrected.getX();
        this.y += corrected.getY();
    }

    private void randomTurn(CollisionDetector detector) {
        if (Math.random() < turnProbability()) {
            if (Math.random() < 0.5)
                turnLeft(detector);
            else
                turnRight(detector);

        } else if (Math.random() < turnBackProbability()) {
            turnBack(detector, false);
        }
    }

    private void turnBack(CollisionDetector detector, boolean ignoreDetector) {
        if (ignoreDetector || canMoveTo(directions.reversed(), detector)) {
            directions = directions.reversed();
        }
    }

    private void turnLeft(CollisionDetector detector) {
        if (canMoveTo(directions.turnedLeft(), detector)) {
            this.directions = directions.turnedLeft();
        }
    }

    private void turnRight(CollisionDetector detector) {
        if (canMoveTo(directions.turnedRight(), detector)) {
            this.directions = directions.turnedRight();
        }
    }

    private boolean isInTileCenter() {
        int centerX = (int) getCenterX() % Tile.WIDTH;
        int centerY = (int) getCenterY() % Tile.HEIGHT;
        int tileCenterX = Tile.WIDTH / 2;
        int tileCenterY = Tile.HEIGHT / 2;
        return (Math.abs(centerX - tileCenterX) <= TRACK_WIDTH &&
                Math.abs(centerY - tileCenterY) <= TRACK_WIDTH);

    }

    private boolean canMoveTo(Directions dirs, CollisionDetector detector) {
        return !detector.isObstacle(this,
                getRow() + dirs.getVerticalDirection(),
                getColumn() + dirs.getHorizontalDirection());
    }

    private double turnProbability() {
        return getType().getTurnProbability();
    }

    private double turnBackProbability() {
        return getType().getTurnBackProbability();
    }

    private double getSpeed() {
        return getType().getSpeed();
    }

    public Type getType() {
        return type;
    }

    public boolean isAlive() {
        return true;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public int width() {
        return WIDTH;
    }

    @Override
    public int height() {
        return HEIGHT;
    }
}
