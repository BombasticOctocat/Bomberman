package com.bombasticoctocat.bomberman;

import com.bombasticoctocat.bomberman.game.Directions;
import com.google.inject.Inject;
import org.slf4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;

public class GameLogicUpdater {
    @InjectLog private static Logger log;
    @Inject GameObjectsManager gameObjectsManager;
    private Thread boardUpdaterThread;
    private final LinkedBlockingQueue<GameUpdate> gameUpdatesQueue = new LinkedBlockingQueue<>();

    private static class GameUpdate {
        public long delta;
        public Directions directions;
        public boolean placedBomb;
        public boolean detonateBomb;
    }

    private void boardUpdater() {
        log.info("Started board updater thread");
        try {
            while (!Thread.currentThread().isInterrupted()) {
                GameUpdate update = gameUpdatesQueue.take();
                gameObjectsManager.getGameLock().lockInterruptibly();
                try {
                    gameObjectsManager.getGame().tick(update.delta, update.directions, update.placedBomb, update.detonateBomb);
                } finally {
                    gameObjectsManager.getGameLock().unlock();
                }
            }
        } catch (InterruptedException e) {
            log.info("Exiting board updater thread");
        }
    }

    public void update(long timeDelta, Directions directions, boolean placedBomb, boolean detonateBomb) {
        if (boardUpdaterThread == null) {
            throw new IllegalStateException("Cannot update logic when thread is stopped");
        }
        GameUpdate update = new GameUpdate();
        update.delta = timeDelta;
        update.placedBomb = placedBomb;
        update.detonateBomb = detonateBomb;
        update.directions = directions;
        try {
            gameUpdatesQueue.put(update);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        gameUpdatesQueue.clear();
        boardUpdaterThread = new Thread(this::boardUpdater);
        boardUpdaterThread.start();
    }

    public void stop() {
        boardUpdaterThread.interrupt();
        boardUpdaterThread = null;
    }
}
