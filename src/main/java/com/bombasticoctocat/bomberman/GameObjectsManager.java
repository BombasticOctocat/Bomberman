package com.bombasticoctocat.bomberman;

import com.bombasticoctocat.bomberman.game.Board;

import java.util.concurrent.locks.ReentrantLock;

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
