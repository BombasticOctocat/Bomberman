package com.bombasticoctocat.bomberman.game;

import java.util.List;

public class Board {
    public static final int FUSE_TIME = 1000;
    public static final int FLAMES_DURATION = 100;
    public static final int TILES_HORIZONTAL = 31;
    public static final int TILES_VERTICAL = 13;
    public static final double DENSITY = 0.3;

    private Hero hero;
    private BoardMap boardMap;
    private CollisionDetector collisionDetector;
    private DeathDetector deathDetector;
    private Timer timer;

    public Board(Timer timer, Hero hero, BoardMap boardMap, CollisionDetector collisionDetector, DeathDetector deathDetector) {
        this.timer = timer;
        this.hero = hero;
        this.boardMap = boardMap;
        this.collisionDetector = collisionDetector;
        this.deathDetector = deathDetector;
    }

    public Board() {
        this.timer = new Timer();
        this.boardMap = new BoardMap(new TilesFactory(TILES_VERTICAL, TILES_HORIZONTAL, DENSITY));
        this.hero = new Hero(new Detonator(boardMap, timer));
        this.collisionDetector = new CollisionDetector(boardMap);
        this.deathDetector = new DeathDetector(boardMap);
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
        return null;
    }

    public void tick(long timeDelta, Directions directions, boolean plantBomb) {
        timer.tick(timeDelta);

        if (plantBomb) {
            hero.plantBomb();
        }

        hero.move(timeDelta, directions, collisionDetector, deathDetector);
    }

}
