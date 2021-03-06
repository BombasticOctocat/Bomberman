package com.bombasticoctocat.bomberman;

import com.google.inject.Inject;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.bombasticoctocat.bomberman.game.Board;
import com.bombasticoctocat.bomberman.game.Tile;

public class MapImageManager {
    @InjectLog private static Logger log;
    private double scale;
    WritableImage mapImage;
    List<List<Tile.Type>> map;
    EnumMap<Tile.Type, ParticleImage> tileMaper = new EnumMap<>(Tile.Type.class);
    Tile anyTile = null;

    @Inject private ParticlesImagesManager particlesImagesManager;
    @Inject GameObjectsManager gameObjectsManager;

    public MapImageManager() {
        for (Tile.Type type: Tile.Type.values()) {
            try {
                tileMaper.put(type, ParticleImage.valueOf(type.toString()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Missing ParticleImage for tile: " + type.toString(), e);
            }
        }
    }

    public void registerOnRefreshParticlesImagesHandler(ParticlesImagesManager particlesImagesManager) {
        particlesImagesManager.setOnRefreshCompleteHandler(this::refreshMapImage);
    }

    public void resetState() {
        gameObjectsManager.getGameLock().lock();
        try {
            Board board = gameObjectsManager.getGame().getBoard();
            anyTile = board.getTileAt(0, 0);
            mapImage = null;
            map = new ArrayList<>();
            for (int i = 0; i < board.tilesVertical(); ++i) {
                List<Tile.Type> row = new ArrayList<>();
                for (int j = 0; j < board.tilesHorizontal(); ++j) {
                    Tile tile = board.getTileAt(j, i);
                    row.add(tile.getType());
                }
                map.add(row);
            }
        } finally {
            gameObjectsManager.getGameLock().unlock();
        }
        refreshMapImage(scale);
    }

    public void renderTileOnImage(PixelWriter pixelWriter, double x, double y, Tile.Type tileType) {
        if (pixelWriter == null) return;
        if (!tileMaper.containsKey(tileType)) {
            throw new RuntimeException("Unknown tile type in MapImageManager");
        }
        WritableImage img = particlesImagesManager.getParticleImage(tileMaper.get(tileType), anyTile);
        if (img != null) {
            pixelWriter.setPixels(
                    (int)(x * anyTile.width() * scale),
                    (int)(y * anyTile.height() * scale),
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
        gameObjectsManager.getGameLock().lock();
        try {
            Board board = gameObjectsManager.getGame().getBoard();
            for (int i = 0; i < board.tilesVertical(); ++i) {
                List<Tile.Type> row = map.get(i);
                for (int j = 0; j < board.tilesHorizontal(); ++j) {
                    Tile tile = board.getTileAt(j, i);
                    if (tile.getType() != row.get(j)) {
                        row.set(j, tile.getType());
                        renderTileOnImage(pixelWriter, j, i, tile.getType());
                    }
                }
            }
        } finally {
            gameObjectsManager.getGameLock().unlock();
        }
    }

    public Void refreshMapImage(Double newScale) {
        scale = newScale;
        Board board = gameObjectsManager.getGame().getBoard();
        if (board == null || scale == 0.0) {
            return null;
        }

        mapImage = new WritableImage((int)(board.width() * scale) + 2,
                (int)(board.height() * scale) + 2);
        PixelWriter pixelWriter = mapImage.getPixelWriter();
        for (int i = 0; i < board.tilesVertical(); ++i) {
            List<Tile.Type> row = map.get(i);
            for (int j = 0; j < board.tilesHorizontal(); ++j) {
                renderTileOnImage(pixelWriter, j, i, row.get(j));
            }
        }
        return null;
    }

    WritableImage getMapImage() {
        return mapImage;
    }
}
