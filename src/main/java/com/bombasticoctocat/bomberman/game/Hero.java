package com.bombasticoctocat.bomberman.game;

/**
 * Created by kustosz on 04/05/14.
 */

public class Hero implements Particle {

    private static final int HEIGHT = 40;
    private static final int WIDTH = 40;
    private static final int INITIAL_X_POSITION = 70;
    private static final int INITIAL_Y_POSITION = 70;
    private static final double SPEED = 1.0;

    private double positionX;
    private double positionY;

    public Hero() {
        positionX = INITIAL_X_POSITION;
        positionY = INITIAL_Y_POSITION;
    }

    public void move(long timeDelta, Directions directions) {
        positionX += directions.getHorizontalDirection() * axisMovement(timeDelta, directions);
        positionY += directions.getVerticalDirection() * axisMovement(timeDelta, directions);
    }

    private double axisMovement(long timeDelta, Directions directions) {
        if (directions.getHorizontalDirection() != 0 && directions.getVerticalDirection() != 0) {
            return timeDelta * speed() / Math.sqrt(2.0);
        } else {
            return timeDelta * speed();
        }
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
}
