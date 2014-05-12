package com.bombasticoctocat.bomberman.game;

import java.util.List;

public class Board {

    private Hero hero;
    private BoardMap boardMap;
    private CollisionDetector collisionDetector;
    private Timer timer;

    public Board(Timer timer, Hero hero, BoardMap boardMap, CollisionDetector collisionDetector) {
        this.timer = timer;
        this.hero = hero;
        this.boardMap = boardMap;
        this.collisionDetector = collisionDetector;
    }

    public Board() {
        this.timer = new Timer();
        this.hero = new Hero();
        this.boardMap = new BoardMap();
        this.collisionDetector = new CollisionDetector(boardMap);
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
            hero.plantBomb(timer, boardMap);
        }

        hero.move(timeDelta, directions, collisionDetector);
    }

}
