package com.bombasticoctocat.bomberman;

import java.net.URL;
import java.util.EnumSet;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import org.slf4j.Logger;

import com.google.inject.Inject;

import com.bombasticoctocat.bomberman.game.*;

public class GameController implements ViewController {
    @InjectLog private static Logger log;
    @FXML private Canvas gameCanvas;
    @FXML private Pane gamePane;
    @Inject ParticlesImagesManager particlesImagesManager;

    private Board board;
    boolean isVisible = false, isRunning = false, isPaused = true, placedBomb = false;
    private double canvasWidth, canvasHeight, boardToCanvasScale;
    private EnumSet<KeyCode> keyboardState = EnumSet.noneOf(KeyCode.class);
    long previousFrameTime;

    private void handleGeometryChange() {
        canvasWidth = gamePane.getWidth();
        canvasHeight = gamePane.getHeight();

        if (canvasWidth <= 0 || canvasHeight <= 0) return;

        gameCanvas.setWidth(canvasWidth);
        gameCanvas.setHeight(canvasHeight);

        boardToCanvasScale = canvasHeight / board.height();

        particlesImagesManager.refreshParticlesImages(boardToCanvasScale);
    }

    private void handleClockTick(ActionEvent event) {
        if (!isRunning || !isVisible) {
            return;
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
            board.tick(currentFrameTime - previousFrameTime, new Directions(directions), placedBomb);
        }
        previousFrameTime = currentFrameTime;
        placedBomb = false;

        Hero hero = board.getHero();

        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);
        gc.drawImage(particlesImagesManager.getParticleImage("character", hero), hero.getX() * boardToCanvasScale, hero.getY() * boardToCanvasScale);

        if (isPaused) {
            gc.setFill(Color.RED);
            gc.setFont(Font.font(12));
            gc.fillText("paused", canvasWidth - 52, 17);
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
        isRunning = true;
        isPaused = false;
        placedBomb = false;
        previousFrameTime = 0;
    }

    public void stopGame() {
        log.info("Stop game");
        board = null;
        isRunning = false;
    }

    @Override
    public void enteredView() {
        log.info("Entered view");
        isVisible = true;
    }

    @Override
    public void leavedView() {
        log.info("Leaved view");
        isVisible = false;
        if (!isPaused) log.info("Paused game");
        isPaused = true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Initialized game controller");
        gamePane.heightProperty().addListener((o, ov, nv) -> handleGeometryChange());
        gamePane.widthProperty().addListener((o, ov, nv) -> handleGeometryChange());

        Timeline game = new Timeline(new KeyFrame(Duration.millis(18.0), this::handleClockTick));
        game.setCycleCount(Timeline.INDEFINITE);
        game.play();

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
