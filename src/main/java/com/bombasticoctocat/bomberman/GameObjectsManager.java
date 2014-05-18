package com.bombasticoctocat.bomberman;

import java.util.concurrent.locks.ReentrantLock;

import com.bombasticoctocat.bomberman.game.Board;
import com.bombasticoctocat.bomberman.game.Game;

public class GameObjectsManager {
    private Game game;
    private final ReentrantLock boardLock = new ReentrantLock();

    public ReentrantLock getBoardLock() {
        return boardLock;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
