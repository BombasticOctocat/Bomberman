package com.bombasticoctocat.bomberman;

import java.util.*;
import java.net.URL;

import javafx.beans.property.BooleanProperty;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

import org.slf4j.Logger;

import com.google.inject.Inject;

import com.bombasticoctocat.bomberman.game.*;

public class GameCanvasRenderer {
    @InjectLog private static Logger log;
    private double boardToCanvasScale;
    private Canvas canvas;
    private double width, height;
    private BooleanProperty isPaused;
    @Inject private ParticlesImagesManager particlesImagesManager;
    @Inject private MapImageManager mapImageManager;
    @Inject private GameObjectsManager gameObjectsManager;
    private final Map<Goomba.Type, ParticleImage> goombaParticleImageMaper = new HashMap<>();
    private static final int HUD_HEIGHT = 42;
    private Font hudFont;
    private Font bigHudFont;
    private Image liveIcon;
    private Set<Runnable> todoAfterGameStart = new HashSet<>();

    public GameCanvasRenderer() {
        canvas = new Canvas(200, 200);

        for (Goomba.Type type : Goomba.Type.values()) {
            try {
                goombaParticleImageMaper.put(type, ParticleImage.valueOf("GOOMBA_" + type.toString()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Missing ParticleImage for goomba: " + type.toString(), e);
            }
        }

        try {
            URL fontUrl = getClass().getResource("fonts/Peralta-Regular.ttf");
            hudFont = Font.loadFont(fontUrl.openStream(), 27);
            bigHudFont = Font.loadFont(fontUrl.openStream(), 40);
            liveIcon = new Image(getClass().getResourceAsStream("images/live_icon.png"));
        } catch (Exception e) {
            throw new RuntimeException("Couldn't load font/image file", e);
        }
    }

    public void initialize(BooleanProperty isPaused) {
        mapImageManager.registerOnRefreshParticlesImagesHandler(particlesImagesManager);
        this.isPaused = isPaused;
    }

    public void resetState() {
        Runnable todo = () -> {
            // quite dirty hack to preaload flames fxml (they don't show up on first explosion without it)
            Board board = gameObjectsManager.getGame().getBoard();
            particlesImagesManager.getParticleImage(ParticleImage.FLAMES, board.getTileAt(0, 0));
            mapImageManager.resetState();
        };
        if (gameObjectsManager.getGame().isLevelInProgress()) {
            todo.run();
        } else {
            todoAfterGameStart.add(todo);
        }
    }

    public Node getCanvasNode() {
        return canvas;
    }

    public void refreshCanvasSize(double newWidth, double newHeight) {
        if (newWidth <= 0 || newHeight <= 0) return;

        width = newWidth;
        height = newHeight - HUD_HEIGHT;
        canvas.setWidth(newWidth);
        canvas.setHeight(newHeight);

        Runnable todo = () -> {
            Board board = gameObjectsManager.getGame().getBoard();
            boardToCanvasScale = Math.max(height / (board.height() * 0.8), width / board.width());
            particlesImagesManager.refreshParticlesImages(boardToCanvasScale);
        };

        if (gameObjectsManager.getGame().isLevelInProgress()) {
            todo.run();
        } else {
            todoAfterGameStart.add(todo);
        }
    }

    private class RedrawManager {
        double wpx, wpy;
        Board board;
        Game game;
        GraphicsContext gc;

        public RedrawManager() {
            game = gameObjectsManager.getGame();
            board = game.getBoard();
            gc = canvas.getGraphicsContext2D();
            computeRenderingWindowPosition();
        }

        private void computeRenderingWindowPosition() {
            Hero hero = board.getHero();
            double heroCenterX = hero.getX() + hero.width() / 2.0;
            double heroCenterY = hero.getY() + hero.height() / 2.0;
            double windowWidth = width / boardToCanvasScale;
            double windowHeight = height / boardToCanvasScale;
            double windowX = Math.min(Math.max(heroCenterX - windowWidth / 2.0, 0.0), board.width() - windowWidth);
            double windowY = Math.min(Math.max(heroCenterY - windowHeight / 2.0, 0.0), board.height() - windowHeight);
            wpx = windowX * boardToCanvasScale;
            wpy = windowY * boardToCanvasScale;
        }

        public void drawBackground(WritableImage backgroungImage) {
            gc.setFill(Color.rgb(22, 45, 80));
            gc.fillRect(0, HUD_HEIGHT, width, height);

            gc.drawImage(backgroungImage, wpx, wpy, width, height,
                                          0, HUD_HEIGHT, width, height);
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
                gc.drawImage(img, particle.getX() * boardToCanvasScale - wpx, particle.getY() * boardToCanvasScale - wpy + HUD_HEIGHT);
            }
        }

        public void drawHUD() {
            gc.setFill(Color.color(0.0, 0.0, 0.0));
            gc.fillRect(0, 0, width, HUD_HEIGHT);

            gc.setFill(Color.color(1.0, 1.0, 1.0));
            gc.setFont(hudFont);
            gc.setTextBaseline(VPos.CENTER);
            gc.setTextAlign(TextAlignment.LEFT);

            gc.fillText("Level " + game.getLevel(), 12, HUD_HEIGHT / 2);
            gc.drawImage(liveIcon, width / 2 - 39, HUD_HEIGHT / 2 - liveIcon.getHeight() / 2);
            gc.fillText("x " + game.getLives(), width / 2 - 5, HUD_HEIGHT / 2);
            gc.fillText("Time: " + board.getTimeLeft() / 1000, width - 153, HUD_HEIGHT / 2);
        }

        public void drawPausedIndicator() {
            gc.setFill(Color.color(0.0, 0.0, 0.0, 0.8));
            gc.fillRoundRect(width / 2 - 130, height / 2 - 60 + HUD_HEIGHT, 260, 120, 17.0, 17.0);

            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.setFill(Color.color(0.9, 0.13, 0.13, 0.9));
            gc.setFont(bigHudFont);
            gc.fillText("paused", width / 2, height / 2 + HUD_HEIGHT);
        }
    }

    private class BlackScreen {
        GraphicsContext gc;

        public BlackScreen() {
            gc = canvas.getGraphicsContext2D();
        }

        public void drawBackground() {
            gc.setFill(Color.color(0.0, 0.0, 0.0));
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }

        public void setText(String text) {
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.setFill(Color.color(0.9, 0.9, 0.9));
            gc.setFont(bigHudFont);
            gc.fillText(text, canvas.getWidth() / 2, canvas.getHeight() / 2);
        }
    }

    public void redraw() {
        gameObjectsManager.getGameLock().lock();
        try {
            Game game = gameObjectsManager.getGame();
            if (game.isLevelInProgress()) {
                if (!todoAfterGameStart.isEmpty()) {
                    Iterator<Runnable> iterator = todoAfterGameStart.iterator();
                    while (iterator.hasNext()) {
                        iterator.next().run();
                        iterator.remove();
                    }
                }

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
                    for (Goomba goomba : goombas) {
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

                redrawManager.drawHUD();

                if (isPaused.get()) {
                    redrawManager.drawPausedIndicator();
                }
            } else {
                BlackScreen blackScreen = new BlackScreen();
                blackScreen.drawBackground();
                if (game.isOver()) {
                    blackScreen.setText("Game Over");
                } else {
                    blackScreen.setText("Level " + game.getLevel());
                }
            }
        } finally {
            gameObjectsManager.getGameLock().unlock();
        }
    }
}
