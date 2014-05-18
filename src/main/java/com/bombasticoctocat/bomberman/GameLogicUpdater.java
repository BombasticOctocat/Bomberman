package com.bombasticoctocat.bomberman;

import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;

import com.google.inject.Inject;

import com.bombasticoctocat.bomberman.game.Directions;

public class GameLogicUpdater {
    @InjectLog private static Logger log;
    @Inject GameObjectsManager gameObjectsManager;
    private Thread boardUpdaterThread;
    private final LinkedBlockingQueue<BoardUpdate> boardUpdatesQueue = new LinkedBlockingQueue<>();

    private static class BoardUpdate {
        public long delta;
        public Directions directions;
        public boolean placedBomb;
    }

    private void boardUpdater() {
        log.info("Started board updater thread");
        try {
            while (!Thread.currentThread().isInterrupted()) {
                BoardUpdate update = boardUpdatesQueue.take();
                gameObjectsManager.getBoardLock().lockInterruptibly();
                try {
                    gameObjectsManager.getGame().getBoard().tick(update.delta, update.directions, update.placedBomb);
                } finally {
                    gameObjectsManager.getBoardLock().unlock();
                }
            }
        } catch (InterruptedException e) {
            log.info("Exiting board updater thread");
        }
    }

    public void update(long timeDelta, Directions directions, boolean placedBomb) {
        if (boardUpdaterThread == null) {
            throw new IllegalStateException("Cannot update logic when thread is stopped");
        }
        BoardUpdate update = new BoardUpdate();
        update.delta = timeDelta;
        update.placedBomb = placedBomb;
        update.directions = directions;
        try {
            boardUpdatesQueue.put(update);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        boardUpdatesQueue.clear();
        boardUpdaterThread = new Thread(this::boardUpdater);
        boardUpdaterThread.start();
    }

    public void stop() {
        boardUpdaterThread.interrupt();
        boardUpdaterThread = null;
    }
}
