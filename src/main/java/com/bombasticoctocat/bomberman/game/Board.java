package com.bombasticoctocat.bomberman.game;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int FUSE_TIME = 1300;
    public static final int END_TIMEOUT = 1000;
    public static final long LEVEL_DURATION = 300 * 1000;
    public static final int FLAMES_DURATION = 230;
    public static final int TILES_HORIZONTAL = 23;
    public static final int TILES_VERTICAL = 11;

    private Hero hero;
    private BoardMap boardMap;
    private CollisionDetector collisionDetector;
    private DeathDetector deathDetector;
    private GoombaTouchDetector goombaTouchDetector;
    private Timer timer;
    private List<Goomba> goombas;
    private Detonator detonator;
    private State state = State.IN_PROGRESS;
    private boolean heroDead = false;
    private boolean won = false;
    public final LevelConfiguration configuration;

    public long getTimeLeft() {
        return timeLeft;
    }

    private long timeLeft = LEVEL_DURATION;

    public enum State {
        IN_PROGRESS, LOST, WON;
    }

    public Board(Timer timer, Hero hero, BoardMap boardMap, CollisionDetector collisionDetector,
                 DeathDetector deathDetector, List<Goomba> goombas, GoombaTouchDetector goombaTouchDetector,
                 Detonator detonator, LevelConfiguration configuration) {
        this.timer = timer;
        this.hero = hero;
        this.boardMap = boardMap;
        this.collisionDetector = collisionDetector;
        this.goombaTouchDetector = goombaTouchDetector;
        this.deathDetector = deathDetector;
        this.goombas = goombas;
        this.detonator = detonator;
        this.configuration = configuration;
    }

    public Board(Timer timer, Hero hero, Detonator detonator, LevelConfiguration configuration) {
        this.timer = timer;
        this.hero = hero;
        this.configuration = configuration;
        this.boardMap = new BoardMap(new TilesFactory(TILES_VERTICAL, TILES_HORIZONTAL, configuration.bricksDensity), configuration.powerup);

        this.detonator = detonator;
        this.detonator.setBoardMap(boardMap);
        this.detonator.clearBombs();

        this.collisionDetector = new CollisionDetector(boardMap);
        this.deathDetector = new DeathDetector(boardMap);
        this.goombas = new ArrayList<>();
        for (Goomba.Type goombaType : configuration.goombas) {
            this.goombas.add(boardMap.placeGoombaAtRandom(goombaType, this));
        }
        this.goombaTouchDetector = new GoombaTouchDetector(goombas, collisionDetector);
    }

    public Timer getTimer() {
        return timer;
    }

    public void removeGoomba(Goomba goomba) {
        goombas.remove(goomba);
    }

    public int tilesHorizontal() {
        return boardMap.tilesHorizontal();
    }

    public int tilesVertical() {
        return boardMap.tilesVertical();
    }

    public int width() {
        return boardMap.width();
    }

    public int height() {
        return boardMap.height();
    }

    public Tile getTileAt(int col, int row) {
        return boardMap.getTileAt(col, row);
    }

    public Hero getHero() {
        return hero;
    }

    public List<Goomba> getGoombas() {
        return goombas;
    }

    public State tick(long timeDelta, Directions directions, boolean plantBomb, boolean detonateBomb) {
        timeLeft -= timeDelta;
        if (!heroDead && timeLeft <= 0)
            hero.die();

        timer.tick(timeDelta);

        if (detonateBomb) {
            detonator.detonateFirst();
        }

        if (plantBomb) {
            hero.plantBomb();
        }

        hero.move(timeDelta, directions, collisionDetector, deathDetector, goombaTouchDetector);
        for (Goomba goomba : getGoombas()) {
            goomba.move(timeDelta, collisionDetector, deathDetector);
        }

        getDoor().update(allGoombasKilled());
        getPowerup().update();


        if (!heroDead && !hero.isAlive()) {
            heroDead = true;
            timer.schedule(END_TIMEOUT, () -> state = State.LOST);
        }

        if (!won && allGoombasKilled() && collisionDetector.areOverlapping(getDoor().getTile(), getHero())) {
            won = true;
            timer.schedule(END_TIMEOUT, () -> state = State.WON);
        }

        if (collisionDetector.areOverlapping(getPowerup().getTile(), getHero())) {
            getPowerup().apply(getHero());
        }

        return state;
    }

    private boolean allGoombasKilled() {
        return goombas.size() == 0;
    }

    private Door getDoor() {
        return boardMap.getDoor();
    }

    private Powerup getPowerup() {
        return boardMap.getPowerup();
    }
}
