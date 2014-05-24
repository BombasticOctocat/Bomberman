package com.bombasticoctocat.bomberman.game;

import java.util.EnumSet;

public class Goomba extends Particle {
    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;
    private static final long DEAD_TIMEOUT = 1000;
    private static final int TRACK_WIDTH = 2;

    public enum Type {
        LEVEL0(0.2, 0.5, 0.1, false),
        LEVEL1(0.3, 0.5, 0.1, false),
        LEVEL2(0.3, 0.2, 0.01, false),
        LEVEL3(0.5, 0.5, 0.1, false),
        LEVEL4(0.1, 0.6, 0.1, true),
        LEVEL5(0.2, 0.5, 0.1, true),
        LEVEL6(0.5, 0.7, 0.1, false),
        LEVEL7(0.5, 0.7, 0.1, true);


        private double speed;
        private double turnProbability;
        private double turnBackProbability;
        private boolean wallpass;

        private Type(double speed, double turnProbability, double turnBackProbability, boolean wallpass) {
            this.turnProbability = turnProbability;
            this.speed = speed;
            this.turnBackProbability = turnBackProbability;
            this.wallpass = wallpass;
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

        public boolean hasWallpass() { return wallpass; }
    }

    private Directions directions;
    private Type type;
    private double x;
    private double y;
    private boolean alive;
    private Board board;

    public Goomba(Board board, Type level, int row, int column) {
        this.board = board;
        this.alive = true;
        this.type = level;
        this.x = column * Tile.WIDTH + (Tile.WIDTH - width()) / 2;
        this.y = row * Tile.HEIGHT + (Tile.HEIGHT - height()) / 2;
        this.directions = new Directions(EnumSet.of(Directions.Direction.RIGHT));
    }

    public void move(long timeDelta, CollisionDetector collisionDetector, DeathDetector deathDetector) {
        if (!isAlive()) return;

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

        if (deathDetector.shouldDie(this)) {
            die();
        }
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

    private void die() {
        board.getTimer().schedule(DEAD_TIMEOUT, () -> board.removeGoomba(this));
        this.alive = false;
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
        return alive;
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

    @Override
    public boolean hasWallpass() {
        return type.hasWallpass();
    }
}
