package com.bombasticoctocat.bomberman.game;

public class Game {
    private Hero hero;
    private Board board;
    private int level = 1;
    private Timer timer;
    private Detonator detonator;

    public Game() {
        this.timer = new Timer();
        this.detonator = new Detonator(timer);
        this.hero = new Hero(detonator);
        this.board = new Board(timer, hero, detonator, Configuration.forLevel(level));
    }

    public void tick(long timeDelta, Directions directions, boolean plantBomb, boolean detonateBomb) {
        Board.State state = board.tick(timeDelta, directions, plantBomb, detonateBomb);
        if (state == Board.State.LOST) {
            hero.revive();
            board = new Board(timer, hero, detonator, Configuration.forLevel(level));
        }

        if (state == Board.State.WON) {
            hero.nextLevel();
            level++;
            board = new Board(timer, hero, detonator, Configuration.forLevel(level));
        }
    }

    public Board getBoard() {
        return board;
    }

    public int getLevel() {
        return level;
    }

    public int getLives() {
        return hero.getLives();
    }

    public boolean isLevelInProgress() {
        return true;
    }

    public boolean isOver() {
        return false;
    }

}
