package com.bombasticoctocat.bomberman.game;

import java.util.Random;

public enum Powerup {
    RANGE() {
        @Override
        public void doApply(Hero hero) {
            hero.getDetonator().increaseRange();
        }
    },
    BOMB() {
        @Override
        public void doApply(Hero hero) {
            hero.getDetonator().addBomb();
        }
    },
    DETONATOR() {
        @Override
        public void doApply(Hero hero) {
            hero.getDetonator().setManualDetonator();
        }
    },
    SPEED() {
        @Override
        public void doApply(Hero hero) {
            hero.increaseSpeed();
        }
    },
    BOMBPASS() {
        @Override
        public void doApply(Hero hero) {
            hero.setBombpass();
        }
    },
    WALLPASS() {
        @Override
        public void doApply(Hero hero) {
            hero.setWallpass();
        }
    },
    FLAMEPASS() {
        @Override
        public void doApply(Hero hero) {
            hero.setFlamepass();
        }
    },
    MYSTERY() {
        @Override
        public void doApply(Hero hero) {
            Random random = new Random();
            Powerup powerup = MYSTERY;
            while (powerup == MYSTERY) {
                powerup = Powerup.values()[random.nextInt(powerup.values().length)];
            }
            powerup.apply(hero);
        }
    };

    protected boolean used = false;
    protected Tile tile;

    public void setTile(Tile tile) {
        this.tile = tile;
        used = false;
    }

    public Tile getTile() {
        return tile;
    }

    public void update() {
        if (tile.getType() != Tile.Type.BRICKS && tile.getType() != Tile.Type.CONCRETE && used == false) {
            tile.setType(Tile.Type.valueOf("POWERUP_" + name()));
        }
    }
    public void apply(Hero hero) {
        if (used == false) {
            used = true;
            getTile().setType(Tile.EMPTY);
            doApply(hero);
        }
    }

    public abstract void doApply(Hero hero);
}
