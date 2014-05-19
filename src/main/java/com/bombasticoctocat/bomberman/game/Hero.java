package com.bombasticoctocat.bomberman.game;

public class Hero extends Particle {
    private static final int HEIGHT = 40;
    private static final int WIDTH = 40;
    private static final int INITIAL_X_POSITION = 70;
    private static final int INITIAL_Y_POSITION = 70;
    private static final int INITIAL_LIVES = 2;
    private static final double SPEED = 0.3;

    private boolean isAlive = true;

    public boolean isMobile() {
        return isMobile;
    }

    private boolean isMobile = true;

    private double positionX;
    private double positionY;
    private int lives;

    public Hero() {
        positionX = INITIAL_X_POSITION;
        positionY = INITIAL_Y_POSITION;
        lives = INITIAL_LIVES;
    }

    public void revive() {
        positionY = INITIAL_Y_POSITION;
        positionX = INITIAL_X_POSITION;
        lives--;
        isAlive = true;
    }

    public void nextLevel() {
        positionY = INITIAL_Y_POSITION;
        positionX = INITIAL_X_POSITION;
        lives++;
        isAlive = true;
        isMobile = true;
    }

    public void move(long timeDelta, Directions directions, CollisionDetector collisionDetector, DeathDetector deathDetector,
                     GoombaTouchDetector goombaTouchDetector) {

        if (!isAlive() || !isMobile())
            return;

        Displacement displacement = new Displacement(
            directions.getHorizontalDirection() * axisMovement(timeDelta, directions),
            directions.getVerticalDirection() * axisMovement(timeDelta, directions)
        );

        displacement = collisionDetector.blockDisplacement(this, displacement);

        move(displacement);

        if (deathDetector.shouldDie(this) || goombaTouchDetector.isTouched(this)) {
            die();
        }
    }

    private void move(Displacement displacement) {
        positionX += displacement.getX();
        positionY += displacement.getY();
    }

    private double axisMovement(long timeDelta, Directions directions) {
        if (directions.getHorizontalDirection() != 0 && directions.getVerticalDirection() != 0) {
            return timeDelta * speed() / Math.sqrt(2.0);
        } else {
            return timeDelta * speed();
        }
    }

    public void die() {
        lives--;
        isAlive = false;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void plantBomb(Detonator detonator) {
        detonator.plantBomb(getColumn(), getRow());
    }

    public double speed() {
        return SPEED;
    }

    @Override
    public double getX() {
        return positionX;
    }

    @Override
    public double getY() {
        return positionY;
    }

    @Override
    public int width() {
        return WIDTH;
    }

    @Override
    public int height() {
        return HEIGHT;
    }

    public int getLives() {
        return lives;
    }
}
