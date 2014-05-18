package com.bombasticoctocat.bomberman.game;

/**
 * Created by marcin on 18/05/14.
 */
public class Game {
    private Hero hero;
    private Board board;
    private int level = 1;
    private Timer timer;

    public Game() {
        this.timer = new Timer();
        this.hero = new Hero();
        this.board = new Board(timer, hero);
    }

    public void tick(long timeDelta, Directions directions, boolean plantBomb) {
        Board.State state = board.tick(timeDelta, directions, plantBomb);
        if (state == Board.State.LOST) {
            hero.revive();
            board = new Board(timer, hero);
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
