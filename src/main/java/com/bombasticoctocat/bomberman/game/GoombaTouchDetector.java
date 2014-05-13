package com.bombasticoctocat.bomberman.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kustosz on 13/05/14.
 */
public class GoombaTouchDetector {

    private List<Goomba> goombas;

    public GoombaTouchDetector(List<Goomba> goombas) {
        this.goombas = goombas;
    }

    public boolean isTouched(Hero hero) {
        for (Goomba goomba : goombas) {
            if (goomba.isAlive() && touching(hero, goomba)) {
                return true;
            }
        }
        return false;
    }

    private boolean touching(Particle p1, Particle p2) {
        if (Math.max(p1.getX() + p1.width(), p2.getX() + p2.width()) - Math.min(p1.getX(), p2.getX()) >=
                p1.width() + p2.width())
            return false;
        if (Math.max(p1.getY() + p1.height(), p2.getY() + p2.height()) - Math.min(p1.getY(), p2.getY()) >=
                p1.height() + p2.height())
            return false;
        return true;
    }
}
