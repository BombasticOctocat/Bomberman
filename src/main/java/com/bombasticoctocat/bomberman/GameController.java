package com.bombasticoctocat.bomberman;

import java.io.IOException;
import java.util.EnumSet;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import org.slf4j.Logger;

import com.google.inject.Inject;

import com.cathive.fx.guice.GuiceFXMLLoader;

import com.bombasticoctocat.bomberman.game.*;

public class GameController {
    @InjectLog private static Logger log;
    @FXML private Canvas gameCanvas;
    @FXML private Pane gamePane;
    @Inject private GuiceFXMLLoader fxmlLoader;

    private Group characterGroup;
    private WritableImage characterImage;
    private Board board;
    private double canvasWidth, canvasHeight, boardToCanvasScale, tickTime = 17.0;
    private EnumSet<KeyCode> keyboardState = EnumSet.noneOf(KeyCode.class);
    private final Object sync = new Object();

    private void handleGeometryChange() {
        synchronized (sync) {
            canvasWidth = gamePane.getWidth();
            canvasHeight = gamePane.getHeight();

            if (canvasWidth <= 0 || canvasHeight <= 0) return;

            gameCanvas.setWidth(canvasWidth);
            gameCanvas.setHeight(canvasHeight);

            boardToCanvasScale = canvasHeight / board.height();

            Hero hero = board.getHero();
            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            parameters.setTransform(new Scale(
                (hero.width() * boardToCanvasScale) / characterGroup.prefWidth(0.0),
                (hero.height() * boardToCanvasScale) / characterGroup.prefHeight(0.0)));

            characterImage = new WritableImage((int)(hero.width() * boardToCanvasScale), (int)(hero.height() * boardToCanvasScale));
            characterGroup.snapshot(parameters, characterImage);
        }
    }

    private void handleClockTick() {
        synchronized (sync) {
            EnumSet<Directions.Direction> directions = EnumSet.noneOf(Directions.Direction.class);
            if (keyboardState.contains(KeyCode.UP)) directions.add(Directions.UP);
            if (keyboardState.contains(KeyCode.LEFT)) directions.add(Directions.LEFT);
            if (keyboardState.contains(KeyCode.RIGHT)) directions.add(Directions.RIGHT);
            if (keyboardState.contains(KeyCode.DOWN)) directions.add(Directions.DOWN);

            board.tick((long) tickTime, new Directions(directions), false);
            Hero hero = board.getHero();

            GraphicsContext gc = gameCanvas.getGraphicsContext2D();
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(0, 0, canvasWidth, canvasHeight);
            gc.drawImage(characterImage, hero.getX() * boardToCanvasScale, hero.getY() * boardToCanvasScale);
        }
    }

    private void handleKeyEvent(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            keyboardState.add(event.getCode());
        } else {
            keyboardState.remove(event.getCode());
        }
    }

    @FXML private void initialize() throws IOException {
        board = new Board();

        characterGroup = fxmlLoader.load(getClass().getResource("fxml/tiles/character.fxml")).getRoot();
        characterGroup.cacheProperty().set(true);

        gamePane.heightProperty().addListener((o, ov, nv) -> handleGeometryChange());
        gamePane.widthProperty().addListener((o, ov, nv) -> handleGeometryChange());

        Timeline game = new Timeline(new KeyFrame(Duration.millis(tickTime), event -> handleClockTick()));
        game.setCycleCount(Timeline.INDEFINITE);

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
            } else {
                game.play();
            }
            if (newScene != null) {
                newScene.setOnKeyPressed(this::handleKeyEvent);
                newScene.setOnKeyReleased(this::handleKeyEvent);
                newScene.getWindow().focusedProperty().addListener(lostFocusWindowListener);
            } else {
                game.stop();
            }
        });
    }
}
