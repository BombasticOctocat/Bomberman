package com.bombasticoctocat.bomberman;

import com.bombasticoctocat.bomberman.game.*;
import com.google.inject.Inject;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.slf4j.Logger;

import java.net.URL;
import java.util.*;

public class GameController implements ViewController {
    @InjectLog private static Logger log;
    @FXML private Pane gamePane;
    @Inject GameCanvasRenderer gameCanvasRenderer;
    @Inject GameObjectsManager gameObjectsManager;
    @Inject GameLogicUpdater gameLogicUpdater;
    private BooleanProperty isPaused = new SimpleBooleanProperty(true);
    private boolean placedBomb = false;
    private final EnumSet<KeyCode> keyboardState = EnumSet.noneOf(KeyCode.class);
    private long previousFrameTime;
    private Timeline gameTimeline;

    private void handleClockTick(ActionEvent event) {
        if (gameObjectsManager.getBoardLock().tryLock()) {
            try {
                gameCanvasRenderer.redraw();

                EnumSet<Directions.Direction> directions = EnumSet.noneOf(Directions.Direction.class);
                for (Settings.DirectionKey dir: Settings.DirectionKey.values()) {
                    if (keyboardState.contains(dir.getSetting())) {
                        directions.add(dir.getDirection());
                    }
                }

                long currentFrameTime = System.currentTimeMillis();
                if (previousFrameTime == 0) {
                    previousFrameTime = currentFrameTime;
                }
                if (!isPaused.get()) {
                    gameLogicUpdater.update(currentFrameTime - previousFrameTime, new Directions(directions), placedBomb);
                }
                previousFrameTime = currentFrameTime;
                placedBomb = false;
            } finally {
                gameObjectsManager.getBoardLock().unlock();
            }
        } else {
            log.warn("Dropped frame");
        }
    }

    private void handleKeyEvent(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            keyboardState.add(event.getCode());
            if (Settings.Key.PAUSE.getSetting() == event.getCode()) {
                isPaused.set(!isPaused.get());
                log.info(isPaused.get() ? "Paused game" : "Unpaused game");
            } else if (Settings.Key.BOMB.getSetting() == event.getCode()) {
                placedBomb = true;
                log.info("Placed bomb");
            }
        } else {
            keyboardState.remove(event.getCode());
        }
    }

    public void startGame() {
        log.info("Start game");
        gameObjectsManager.setBoard(new Board());
        gameCanvasRenderer.resetState();
        gameLogicUpdater.start();
        placedBomb = false;
        isPaused.set(false);
    }

    public void stopGame() {
        log.info("Stop game");
        gameLogicUpdater.stop();
        gameObjectsManager.setBoard(null);
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
        if (!isPaused.get()) log.info("Paused game");
        isPaused.set(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final ChangeListener<Number> geometryChangeListener = (observable, oldValue, newValue) -> {
            gameCanvasRenderer.refreshCanvasSize(gamePane.getWidth(), gamePane.getHeight());
        };

        gamePane.heightProperty().addListener(geometryChangeListener);
        gamePane.widthProperty().addListener(geometryChangeListener);

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

        gameCanvasRenderer.initialize(isPaused);

        gamePane.getChildren().setAll(gameCanvasRenderer.getCanvasNode());

        log.info("Initialized game controller");
    }
}
