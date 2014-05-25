package com.bombasticoctocat.bomberman;

import java.util.concurrent.locks.ReentrantLock;

import com.bombasticoctocat.bomberman.game.Game;

public class GameObjectsManager {
    private Game game;
    private final ReentrantLock gameLock = new ReentrantLock();

    public ReentrantLock getGameLock() {
        return gameLock;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
