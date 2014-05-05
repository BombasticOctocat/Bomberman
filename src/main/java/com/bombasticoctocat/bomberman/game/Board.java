package com.bombasticoctocat.bomberman.game;

import java.util.List;

/**
 * Created by kustosz on 04/05/14.
 */

public class Board {

    private Hero hero;
    private BoardMap boardMap;

    public Board(Hero hero, BoardMap boardMap) {
        this.hero = hero;
        this.boardMap = boardMap;
    }

    public Board() {
        this(new Hero(), new BoardMap());
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

    public void tick(long timeDelta, Directions directions, boolean bombPlanted) {
        hero.move(timeDelta, directions);
    }

}
