package com.bombasticoctocat.bomberman.game;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int FUSE_TIME = 1000;
    public static final int END_TIMEOUT = 1000;
    public static final long LEVEL_DURATION = 300 * 1000;
    public static final int FLAMES_DURATION = 100;
    public static final int TILES_HORIZONTAL = 31;
    public static final int TILES_VERTICAL = 13;
    public static final double DENSITY = 0.3;

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

    public long getTimeLeft() {
        return timeLeft;
    }

    private long timeLeft = LEVEL_DURATION;

    public enum State {
        IN_PROGRESS, LOST, WON;
    }

    public Board(Timer timer, Hero hero, BoardMap boardMap, CollisionDetector collisionDetector,
                 DeathDetector deathDetector, List<Goomba> goombas, GoombaTouchDetector goombaTouchDetector, Detonator detonator) {
        this.timer = timer;
        this.hero = hero;
        this.boardMap = boardMap;
        this.collisionDetector = collisionDetector;
        this.goombaTouchDetector = goombaTouchDetector;
        this.deathDetector = deathDetector;
        this.goombas = goombas;
        this.detonator = detonator;
    }

    public Board(Timer timer, Hero hero) {
        this.timer = timer;
        this.hero = hero;
        this.boardMap = new BoardMap(new TilesFactory(TILES_VERTICAL, TILES_HORIZONTAL, DENSITY));
        this.detonator = new Detonator(boardMap, timer);
        this.collisionDetector = new CollisionDetector(boardMap);
        this.deathDetector = new DeathDetector(boardMap);
        this.goombas = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            this.goombas.add(boardMap.placeGoombaAtRandom(Goomba.Type.LEVEL0, this));
        }
        this.goombaTouchDetector = new GoombaTouchDetector(goombas, collisionDetector);
    }

    public Board() {
        this(new Timer(), new Hero());
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

    public State tick(long timeDelta, Directions directions, boolean plantBomb) {
        timeLeft -= timeDelta;
        if (timeLeft <= 0)
            hero.die();

        timer.tick(timeDelta);

        if (plantBomb) {
            hero.plantBomb(detonator);
        }

        hero.move(timeDelta, directions, collisionDetector, deathDetector, goombaTouchDetector);
        for (Goomba goomba : getGoombas()) {
            goomba.move(timeDelta, collisionDetector, deathDetector);
        }

        getDoor().update(allGoombasKilled());

        if (!heroDead && !hero.isAlive()) {
            heroDead = true;
            timer.schedule(END_TIMEOUT, () -> state = State.LOST);
        }

        if (!won && allGoombasKilled() && collisionDetector.areOverlapping(getDoor().getTile(), getHero())) {
            won = true;
            timer.schedule(END_TIMEOUT, () -> state = State.WON);
        }

        return state;
    }

    private boolean allGoombasKilled() {
        return goombas.size() == 0;
    }

    private Door getDoor() {
        return boardMap.getDoor();
    }
}
