package com.bombasticoctocat.bomberman;

import java.net.URL;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
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
    private boolean rerenderCanvas;
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

        boardToCanvasScale = Math.max(canvasHeight / board.height(), canvasWidth / board.width());

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
                    rerenderCanvas = true;
                } finally {
                    boardLock.unlock();
                }
            }
        } catch (InterruptedException e) {
            log.info("Exiting board updater thread");
        }
    }

    private class MapImageManager {
        WritableImage mapImage;
        List<List<Tile>> map;
        EnumMap<Tile.Type, String> tileMaper = new EnumMap<>(Tile.Type.class);

        MapImageManager() {
            tileMaper.put(Tile.CONCRETE, "concrete");
            tileMaper.put(Tile.EMPTY, "empty");
            tileMaper.put(Tile.BRICKS, "bricks");
        }

        public void registerOnRefreshParticlesImagesHandler(ParticlesImagesManager particlesImagesManager) {
            particlesImagesManager.setOnRefreshCompleteHandler(this::refreshMapImage);
        }

        public void initialize() {
            boardLock.lock();
            try {
                mapImage = null;
                map = new ArrayList<>();
                for (int i = 0; i < board.tilesHorizontal(); ++i) {
                    List<Tile> row = new ArrayList<>();
                    for (int j = 0; j < board.tilesVertical(); ++j) {
                        Tile tile = board.getTileAt(i, j);
                        row.add(tile);
                    }
                    map.add(row);
                }
                log.debug("initialized MapImageManager");
            } finally {
                boardLock.unlock();
            }
            refreshMapImage();
        }

        public void renderTileOnImage(PixelWriter pixelWriter, Tile tile) {
            if (pixelWriter == null) return;
            WritableImage img = particlesImagesManager.getParticleImage(tileMaper.get(tile.getType()), tile);
            if (img != null) {
                pixelWriter.setPixels(
                        (int)(tile.getX() * boardToCanvasScale),
                        (int)(tile.getY() * boardToCanvasScale),
                        (int)img.getWidth(),
                        (int)img.getHeight(),
                        img.getPixelReader(), 0, 0);
            }
        }

        public void refreshMapImageTiles() {
            PixelWriter pixelWriter = null;
            if (mapImage != null) {
                pixelWriter = mapImage.getPixelWriter();
            }
            boardLock.lock();
            try {
                for (int i = 0; i < board.tilesHorizontal(); ++i) {
                    List<Tile> row = map.get(i);
                    for (int j = 0; j < board.tilesVertical(); ++j) {
                        Tile tile = board.getTileAt(i, j);
                        if (tile.getType() != row.get(j).getType()) {
                            row.set(j, tile);
                            renderTileOnImage(pixelWriter, tile);
                        }
                    }
                }
            } finally {
                boardLock.unlock();
            }
        }

        public void refreshMapImage() {
            if (board == null || boardToCanvasScale == 0.0) {
                return;
            }

            mapImage = new WritableImage((int)(board.width() * boardToCanvasScale) + 1,
                                         (int)(board.height() * boardToCanvasScale) + 1);
            PixelWriter pixelWriter = mapImage.getPixelWriter();
            for (int i = 0; i < board.tilesHorizontal(); ++i) {
                List<Tile> row = map.get(i);
                for (int j = 0; j < board.tilesVertical(); ++j) {
                    renderTileOnImage(pixelWriter, row.get(j));
                }
            }
            log.debug("refreshed image size MapImageManager");
        }

        WritableImage getMapImage() {
            return mapImage;
        }
    }
    private MapImageManager mapImageManager = new MapImageManager();

    private void canvasRenderer() {
        boardLock.lock();
        try {
            GraphicsContext gc = gameCanvas.getGraphicsContext2D();
            gc.setFill(Color.rgb(22, 45, 80));
            gc.fillRect(0, 0, canvasWidth, canvasHeight);

            mapImageManager.refreshMapImageTiles();

            WritableImage mapImage = mapImageManager.getMapImage();
            if (mapImage != null) {
                gc.drawImage(mapImage, 0, 0, canvasWidth, canvasHeight,
                                       0, 0, canvasWidth, canvasHeight);
            }

            for (int i = 0; i < board.tilesHorizontal(); ++i) {
                for (int j = 0; j < board.tilesVertical(); ++j) {
                    Tile tile = board.getTileAt(i, j);
                    // TODO: bomb, bonus, flames etc rendering
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

            Hero hero = board.getHero();

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
        if (boardLock.tryLock()) {
            try {
                if (rerenderCanvas) {
                    rerenderCanvas = false;
                    canvasRenderer();
                }

                EnumSet<Directions.Direction> directions = EnumSet.noneOf(Directions.Direction.class);
                if (keyboardState.contains(KeyCode.UP)) directions.add(Directions.UP);
                if (keyboardState.contains(KeyCode.LEFT)) directions.add(Directions.LEFT);
                if (keyboardState.contains(KeyCode.RIGHT)) directions.add(Directions.RIGHT);
                if (keyboardState.contains(KeyCode.DOWN)) directions.add(Directions.DOWN);

                long currentFrameTime = System.currentTimeMillis();
                if (previousFrameTime == 0) {
                    previousFrameTime = currentFrameTime;
                }
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
                    rerenderCanvas = true;
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
        mapImageManager.initialize();
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
        rerenderCanvas = false;
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

        mapImageManager.registerOnRefreshParticlesImagesHandler(particlesImagesManager);

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
