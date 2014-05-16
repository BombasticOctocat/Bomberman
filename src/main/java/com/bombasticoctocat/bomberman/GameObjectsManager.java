package com.bombasticoctocat.bomberman;

import java.util.concurrent.locks.ReentrantLock;

import com.bombasticoctocat.bomberman.game.Board;

public class GameObjectsManager {
    private Board board;
    private final ReentrantLock boardLock = new ReentrantLock();

    public ReentrantLock getBoardLock() {
        return boardLock;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
