package com.bombasticoctocat.bomberman.game;

/**
 * Created by kustosz on 04/05/14.
 */

public class Hero extends Particle {

    private static final int HEIGHT = 40;
    private static final int WIDTH = 40;
    private static final int INITIAL_X_POSITION = 70;
    private static final int INITIAL_Y_POSITION = 70;
    private static final double SPEED = 0.3;

    private double positionX;
    private double positionY;

    public Hero() {
        positionX = INITIAL_X_POSITION;
        positionY = INITIAL_Y_POSITION;
    }

    public void move(long timeDelta, Directions directions, CollisionDetector collisionDetector) {
        Displacement displacement = new Displacement(
            directions.getHorizontalDirection() * axisMovement(timeDelta, directions),
            directions.getVerticalDirection() * axisMovement(timeDelta, directions)
        );

        displacement = collisionDetector.blockDisplacement(this, displacement);

        move(displacement);
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


    public void plantBomb(BoardMap boardMap) {
        boardMap.plantBomb(getColumn(), getRow());
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
