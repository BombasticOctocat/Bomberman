package com.bombasticoctocat.bomberman;

import com.bombasticoctocat.bomberman.game.*;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.google.inject.Inject;
import org.slf4j.Logger;

public class GameCanvasRenderer {
    @InjectLog private static Logger log;
    private double boardToCanvasScale;
    private Canvas canvas;
    private boolean isPaused;
    @Inject private ParticlesImagesManager particlesImagesManager;
    @Inject private MapImageManager mapImageManager;
    @Inject private GameObjectsManager gameObjectsManager;

    public GameCanvasRenderer() {
        canvas = new Canvas(200, 200);
    }

    public void initialize() {
        mapImageManager.registerOnRefreshParticlesImagesHandler(particlesImagesManager);
    }

    public void resetState() {
        // quite dirty hack to preaload flames fxml (they don't show up on first explosion without it)
        Board board = gameObjectsManager.getBoard();
        particlesImagesManager.getParticleImage(ParticleImage.FLAMES, board.getTileAt(0, 0));
        mapImageManager.resetState();
    }

    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public Node getCanvasNode() {
        return canvas;
    }

    public void refreshCanvasSize(double width, double height) {
        if (width <= 0 || height <= 0) return;

        canvas.setWidth(width);
        canvas.setHeight(height);

        Board board = gameObjectsManager.getBoard();

        boardToCanvasScale = Math.max(canvas.getHeight() / (board.height() * 0.8), canvas.getWidth() / board.width());

        particlesImagesManager.refreshParticlesImages(boardToCanvasScale);
    }

    public void redraw() {
        gameObjectsManager.getBoardLock().lock();
        try {
            Board board = gameObjectsManager.getBoard();
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.rgb(22, 45, 80));
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

            Hero hero = board.getHero();
            double wpx, wpy; //rendering window pos {x,y}
            {
                double heroCenterX = hero.getX() + hero.width() / 2.0;
                double heroCenterY = hero.getY() + hero.height() / 2.0;
                double windowWidth = canvas.getWidth() / boardToCanvasScale;
                double windowHeight = canvas.getHeight() / boardToCanvasScale;
                double windowX = Math.min(Math.max(heroCenterX - windowWidth / 2.0, 0.0), board.width() - windowWidth);
                double windowY = Math.min(Math.max(heroCenterY - windowHeight / 2.0, 0.0), board.height() - windowHeight);
                wpx = windowX * boardToCanvasScale;
                wpy = windowY * boardToCanvasScale;
            }

            mapImageManager.refreshMapImageTiles();

            WritableImage mapImage = mapImageManager.getMapImage();
            if (mapImage != null) {
                gc.drawImage(mapImage, wpx, wpy, canvas.getWidth(), canvas.getHeight(),
                        0, 0, canvas.getWidth(), canvas.getHeight());
            }

            for (int i = 0; i < board.tilesHorizontal(); ++i) {
                for (int j = 0; j < board.tilesVertical(); ++j) {
                    Tile tile = board.getTileAt(i, j);

                    if (tile.isOnFire()) {
                        WritableImage img = particlesImagesManager.getParticleImage(ParticleImage.FLAMES, tile);
                        if (img != null) {
                            gc.drawImage(img, tile.getX() * boardToCanvasScale - wpx, tile.getY() * boardToCanvasScale - wpy);
                        }
                    }

                    if (tile.isBombPlanted()) {
                        WritableImage img = particlesImagesManager.getParticleImage(ParticleImage.BOMB, tile);
                        if (img != null) {
                            gc.drawImage(img, tile.getX() * boardToCanvasScale - wpx, tile.getY() * boardToCanvasScale - wpy);
                        }
                    }
                }
            }

            List<Goomba> goombas = board.getGoombas();
            if (goombas != null) {
                for (Goomba goomba: goombas) {
                    WritableImage img = particlesImagesManager.getParticleImage(goomba.isAlive() ? ParticleImage.GOOMBA : ParticleImage.KILLED, goomba);
                    if (img != null) {
                        gc.drawImage(img, goomba.getX() * boardToCanvasScale - wpx, goomba.getY() * boardToCanvasScale - wpy);
                    }
                }
            }

            WritableImage img = particlesImagesManager.getParticleImage(hero.isAlive() ? ParticleImage.CHARACTER : ParticleImage.KILLED, hero);
            if (img != null) {
                gc.drawImage(img, hero.getX() * boardToCanvasScale - wpx, hero.getY() * boardToCanvasScale - wpy);
            }

            if (isPaused) {
                gc.setFill(Color.color(0.0, 0.0, 0.0, 0.6));
                gc.fillRect(canvas.getWidth() - 94, 0, canvas.getWidth() - 94, 28);
                gc.setFill(Color.color(0.9, 0.1, 0.1, 1.0));
                gc.setFont(Font.font("System", FontWeight.BOLD, 20));
                gc.fillText("paused", canvas.getWidth() - 87, 20);
            }
        } finally {
            gameObjectsManager.getBoardLock().unlock();
        }
    }
}
