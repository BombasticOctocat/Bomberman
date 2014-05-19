package com.bombasticoctocat.bomberman.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kustosz on 13/05/14.
 */
public class GoombaTouchDetector {

    private List<Goomba> goombas;
    private CollisionDetector collisionDetector;

    public GoombaTouchDetector(List<Goomba> goombas, CollisionDetector collisionDetector) {
        this.goombas = goombas;
        this.collisionDetector = collisionDetector;
    }

    public boolean isTouched(Hero hero) {
        for (Goomba goomba : goombas) {
            if (goomba.isAlive() && collisionDetector.areOverlapping(hero, goomba)) {
                return true;
            }
        }
        return false;
    }
}
