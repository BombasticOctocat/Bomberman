package com.bombasticoctocat.bomberman;

import java.net.URL;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import org.slf4j.Logger;

import com.google.inject.Inject;

import com.bombasticoctocat.bomberman.game.*;

public class GameController implements ViewController {
    @InjectLog private static Logger log;
    @FXML private Canvas gameCanvas;
    @FXML private Pane gamePane;
    @Inject private ParticlesImagesManager particlesImagesManager;

    private Board board;
    private boolean isPaused = true, placedBomb = false;
    private double canvasWidth, canvasHeight, boardToCanvasScale;
    private final EnumSet<KeyCode> keyboardState = EnumSet.noneOf(KeyCode.class);
    private long previousFrameTime;
    private final ReentrantLock boardLock = new ReentrantLock();
    private Thread boardUpdaterThread;
    private final LinkedBlockingQueue<BoardUpdate> boardUpdatesQueue = new LinkedBlockingQueue<>();
    private Timeline gameTimeline;

    private static class BoardUpdate {
        public long delta;
        public Directions directions;
        public boolean placedBomb;
    }

    private void handleGeometryChange() {
        canvasWidth = gamePane.getWidth();
        canvasHeight = gamePane.getHeight();

        if (canvasWidth <= 0 || canvasHeight <= 0) return;

        gameCanvas.setWidth(canvasWidth);
        gameCanvas.setHeight(canvasHeight);

        boardToCanvasScale = canvasHeight / board.height();

        particlesImagesManager.refreshParticlesImages(boardToCanvasScale);
    }

    private void boardUpdater() {
        log.info("Started board updater thread");
        try {
            while (!Thread.currentThread().isInterrupted()) {
                BoardUpdate update = boardUpdatesQueue.take();
                boardLock.lockInterruptibly();
                try {
                    board.tick(update.delta, update.directions, update.placedBomb);
                } finally {
                    boardLock.unlock();
                }
                Platform.runLater(this::canvasRenderer);
            }
        } catch (InterruptedException e) {
            log.info("Exiting board updater thread");
        }
    }

    private void canvasRenderer() {
        boardLock.lock();
        try {
            GraphicsContext gc = gameCanvas.getGraphicsContext2D();
            gc.setFill(Color.web("#162d50"));
            gc.fillRect(0, 0, canvasWidth, canvasHeight);

            Hero hero = board.getHero();

            EnumMap<Tile.Type, String> tileMaper = new EnumMap<>(Tile.Type.class);
            tileMaper.put(Tile.CONCRETE, "concrete");
            tileMaper.put(Tile.EMPTY, "empty");
            tileMaper.put(Tile.BRICKS, "bricks");
            for (int i = 0; i < board.tilesHorizontal(); ++i) {
                for (int j = 0; j < board.tilesVertical(); ++j) {
                    Tile tile = board.getTileAt(i, j);
                    WritableImage img = particlesImagesManager.getParticleImage(tileMaper.get(tile.getType()), tile);
                    if (img != null) {
                        gc.drawImage(img, tile.getX() * boardToCanvasScale, tile.getY() * boardToCanvasScale);
                    }
                }
            }

            List<Goomba> goombas = board.getGoombas();
            if (goombas != null) {
                for (Goomba goomba: goombas) {
                    WritableImage img = particlesImagesManager.getParticleImage("goomba", goomba);
                    if (img != null) {
                        gc.drawImage(img, goomba.getX() * boardToCanvasScale, goomba.getY() * boardToCanvasScale);
                    }
                }
            }

            WritableImage img = particlesImagesManager.getParticleImage("character", hero);
            if (img != null) {
                gc.drawImage(img, hero.getX() * boardToCanvasScale, hero.getY() * boardToCanvasScale);
            }

            if (isPaused) {
                gc.setFill(Color.color(0.0, 0.0, 0.0, 0.6));
                gc.fillRect(canvasWidth - 94, 0, canvasWidth - 94, 28);
                gc.setFill(Color.color(0.9, 0.1, 0.1, 1.0));
                gc.setFont(Font.font("System", FontWeight.BOLD, 20));
                gc.fillText("paused", canvasWidth - 87, 20);
            }
        } finally {
            boardLock.unlock();
        }
    }

    private void handleClockTick(ActionEvent event) {
        EnumSet<Directions.Direction> directions = EnumSet.noneOf(Directions.Direction.class);
        if (keyboardState.contains(KeyCode.UP)) directions.add(Directions.UP);
        if (keyboardState.contains(KeyCode.LEFT)) directions.add(Directions.LEFT);
        if (keyboardState.contains(KeyCode.RIGHT)) directions.add(Directions.RIGHT);
        if (keyboardState.contains(KeyCode.DOWN)) directions.add(Directions.DOWN);

        long currentFrameTime = System.currentTimeMillis();
        if (previousFrameTime == 0) {
            previousFrameTime = currentFrameTime;
        }
        if (boardLock.tryLock()) {
            try {
                if (!isPaused) {
                    BoardUpdate update = new BoardUpdate();
                    update.delta = currentFrameTime - previousFrameTime;
                    update.placedBomb = placedBomb;
                    update.directions = new Directions(directions);
                    try {
                        boardUpdatesQueue.put(update);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    canvasRenderer();
                }
                previousFrameTime = currentFrameTime;
                placedBomb = false;
            } finally {
                boardLock.unlock();
            }
        } else {
            log.warn("Dropped frame");
        }
    }

    private void handleKeyEvent(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            keyboardState.add(event.getCode());
            switch (event.getCode()) {
                case P:
                    isPaused = !isPaused;
                    log.info(isPaused ? "Paused game" : "Unpaused game");
                    break;
                case Z:
                    placedBomb = true;
                    log.info("Placed bomb");
            }
        } else {
            keyboardState.remove(event.getCode());
        }
    }

    public void startGame() {
        log.info("Start game");
        board = new Board();
        boardUpdatesQueue.clear();
        boardUpdaterThread = new Thread(this::boardUpdater);
        boardUpdaterThread.start();
        placedBomb = false;
        isPaused = false;
    }

    public void stopGame() {
        log.info("Stop game");
        boardUpdaterThread.interrupt();
        boardUpdaterThread = null;
        board = null;
    }

    @Override
    public void enteredView() {
        log.info("Entered view");
        previousFrameTime = 0;
        gameTimeline.play();
    }

    @Override
    public void leavedView() {
        log.info("Leaved view");
        gameTimeline.stop();
        if (!isPaused) log.info("Paused game");
        isPaused = true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Initialized game controller");
        gamePane.heightProperty().addListener((o, ov, nv) -> handleGeometryChange());
        gamePane.widthProperty().addListener((o, ov, nv) -> handleGeometryChange());

        gameTimeline = new Timeline(new KeyFrame(Duration.millis(18.0), this::handleClockTick));
        gameTimeline.setCycleCount(Timeline.INDEFINITE);

        final ChangeListener<Boolean> lostFocusWindowListener = (ob, ov, focused) -> {
            if (!focused) {
                keyboardState.clear();
            }
        };

        gamePane.sceneProperty().addListener((o, oldScene, newScene) -> {
            if (oldScene != null) {
                oldScene.setOnKeyPressed(null);
                oldScene.setOnKeyReleased(null);
                oldScene.getWindow().focusedProperty().removeListener(lostFocusWindowListener);
            }
            if (newScene != null) {
                newScene.setOnKeyPressed(this::handleKeyEvent);
                newScene.setOnKeyReleased(this::handleKeyEvent);
                newScene.getWindow().focusedProperty().addListener(lostFocusWindowListener);
            }
        });
    }
}
