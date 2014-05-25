package com.bombasticoctocat.bomberman.game;

public class Game {
    private Hero hero;
    private Board board;
    private int level = 1;
    private Timer timer;
    private Detonator detonator;
    private boolean over;
    private boolean won;

    private static final long LEVEL_DELAY = 1000;

    public Game() {
        this.timer = new Timer();
        this.detonator = new Detonator(timer);
        this.hero = new Hero(detonator);
        timer.schedule(LEVEL_DELAY, () -> startLevel());
        this.over = false;
        this.won = false;
    }

    private void startLevel() {
        timer.clear();
        board = new Board(timer, hero, detonator, Configuration.forLevel(level));
    }

    public void tick(long timeDelta, Directions directions, boolean plantBomb, boolean detonateBomb) {
        if (board == null) {
            timer.tick(timeDelta);
            return;
        }

        Board.State state = board.tick(timeDelta, directions, plantBomb, detonateBomb);
        if (state == Board.State.LOST) {
            board = null;
            hero.revive();
            if (hero.getLives() < 0) {
                over = true;
            } else {
                timer.schedule(LEVEL_DELAY, () -> startLevel());
            }
        }

        if (state == Board.State.WON) {
            board = null;
            hero.nextLevel();
            level++;
            if (level > 50) {
                board = null;
                won = true;
            } else {
                timer.schedule(LEVEL_DELAY, () -> startLevel());
            }
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
        return board != null;
    }

    public boolean isOver() {
        return over;
    }

    public boolean isWon() { return won; }

}
