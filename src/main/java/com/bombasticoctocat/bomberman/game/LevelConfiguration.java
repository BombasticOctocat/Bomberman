package com.bombasticoctocat.bomberman.game;

import java.util.ArrayList;

public class LevelConfiguration {
    public final int level;
    public final double bricksDensity;
    public final ArrayList<Goomba.Type> goombas;
    public final Powerup powerup;
    public final int penaltyGoombas;
    public final int penaltyGoombaLevel;

    public LevelConfiguration(int level, double bricksDensity, ArrayList<Goomba.Type> goombas, Powerup powerup, int penaltyGoombas, int penaltyGoombaLevel) {
        this.level = level;
        this.bricksDensity = bricksDensity;
        this.goombas = goombas;
        this.powerup = powerup;
        this.penaltyGoombas = penaltyGoombas;
        this.penaltyGoombaLevel = penaltyGoombaLevel;
    }
}
