package com.bombasticoctocat.bomberman;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;

import org.slf4j.Logger;

import com.google.inject.Inject;

import com.bombasticoctocat.bomberman.game.*;

public class GameCanvasRenderer {
    @InjectLog private static Logger log;
    private double boardToCanvasScale;
    private Canvas canvas;
    private BooleanProperty isPaused;
    @Inject private ParticlesImagesManager particlesImagesManager;
    @Inject private MapImageManager mapImageManager;
    @Inject private GameObjectsManager gameObjectsManager;
    private final Map<Goomba.Type, ParticleImage> goombaParticleImageMaper = new HashMap<>();

    public GameCanvasRenderer() {
        canvas = new Canvas(200, 200);
        for (Goomba.Type type: Goomba.Type.values()) {
            try {
                goombaParticleImageMaper.put(type, ParticleImage.valueOf("GOOMBA_" + type.toString()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Missing ParticleImage for goomba: " + type.toString(), e);
            }
        }
    }

    public void initialize(BooleanProperty isPaused) {
        mapImageManager.registerOnRefreshParticlesImagesHandler(particlesImagesManager);
        this.isPaused = isPaused;
    }

    public void resetState() {
        // quite dirty hack to preaload flames fxml (they don't show up on first explosion without it)
        Board board = gameObjectsManager.getGame().getBoard();
        particlesImagesManager.getParticleImage(ParticleImage.FLAMES, board.getTileAt(0, 0));
        mapImageManager.resetState();
    }

    public Node getCanvasNode() {
        return canvas;
    }

    public void refreshCanvasSize(double width, double height) {
        if (width <= 0 || height <= 0) return;

        canvas.setWidth(width);
        canvas.setHeight(height);

        Board board = gameObjectsManager.getGame().getBoard();

        boardToCanvasScale = Math.max(canvas.getHeight() / (board.height() * 0.8), canvas.getWidth() / board.width());

        particlesImagesManager.refreshParticlesImages(boardToCanvasScale);
    }

    private class RedrawManager {
        double wpx, wpy;
        double width, height;
        Board board;
        GraphicsContext gc;

        public RedrawManager() {
            board = gameObjectsManager.getGame().getBoard();
            gc = canvas.getGraphicsContext2D();
            computeRenderingWindowPosition();
        }

        private void computeRenderingWindowPosition() {
            Hero hero = board.getHero();
            double heroCenterX = hero.getX() + hero.width() / 2.0;
            double heroCenterY = hero.getY() + hero.height() / 2.0;
            double windowWidth = canvas.getWidth() / boardToCanvasScale;
            double windowHeight = canvas.getHeight() / boardToCanvasScale;
            double windowX = Math.min(Math.max(heroCenterX - windowWidth / 2.0, 0.0), board.width() - windowWidth);
            double windowY = Math.min(Math.max(heroCenterY - windowHeight / 2.0, 0.0), board.height() - windowHeight);
            wpx = windowX * boardToCanvasScale;
            wpy = windowY * boardToCanvasScale;
        }

        public void drawBackground(WritableImage backgroungImage) {
            gc.setFill(Color.rgb(22, 45, 80));
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

            gc.drawImage(backgroungImage, wpx, wpy, canvas.getWidth(), canvas.getHeight(),
                                          0, 0, canvas.getWidth(), canvas.getHeight());
        }

        public void forEveryTile(Callback<Tile, Void> callback) {
            for (int i = 0; i < board.tilesHorizontal(); ++i) {
                for (int j = 0; j < board.tilesVertical(); ++j) {
                    Tile tile = board.getTileAt(i, j);
                    callback.call(tile);
                }
            }
        }

        public void drawParticle(ParticleImage image, Particle particle) {
            WritableImage img = particlesImagesManager.getParticleImage(image, particle);
            if (img != null) {
                gc.drawImage(img, particle.getX() * boardToCanvasScale - wpx, particle.getY() * boardToCanvasScale - wpy);
            }
        }

        public void drawPausedIndicator() {
            gc.setFill(Color.color(0.0, 0.0, 0.0, 0.6));
            gc.fillRect(canvas.getWidth() - 94, 0, canvas.getWidth() - 94, 28);
            gc.setFill(Color.color(0.9, 0.1, 0.1, 1.0));
            gc.setFont(Font.font("System", FontWeight.BOLD, 20));
            gc.fillText("paused", canvas.getWidth() - 87, 20);
        }
    }

    public void redraw() {
        gameObjectsManager.getBoardLock().lock();
        try {
            RedrawManager redrawManager = new RedrawManager();

            mapImageManager.refreshMapImageTiles();

            WritableImage mapImage = mapImageManager.getMapImage();
            if (mapImage != null) {
                redrawManager.drawBackground(mapImage);
            }

            redrawManager.forEveryTile(tile -> {
                if (tile.isOnFire()) {
                    redrawManager.drawParticle(ParticleImage.FLAMES, tile);
                }
                if (tile.isBombPlanted()) {
                    redrawManager.drawParticle(ParticleImage.BOMB, tile);
                }
                return null;
            });

            Board board = gameObjectsManager.getGame().getBoard();

            List<Goomba> goombas = board.getGoombas();
            if (goombas != null) {
                for (Goomba goomba: goombas) {
                    ParticleImage image;
                    if (goomba.isAlive()) {
                        image = goombaParticleImageMaper.get(goomba.getType());
                    } else {
                        image = ParticleImage.KILLED;
                    }
                    redrawManager.drawParticle(image, goomba);
                }
            }

            Hero hero = board.getHero();
            redrawManager.drawParticle(hero.isAlive() ? ParticleImage.CHARACTER : ParticleImage.KILLED, hero);

            if (isPaused.get()) {
                redrawManager.drawPausedIndicator();
            }
        } finally {
            gameObjectsManager.getBoardLock().unlock();
        }
    }
}
