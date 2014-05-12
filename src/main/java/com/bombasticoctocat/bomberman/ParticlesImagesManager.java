package com.bombasticoctocat.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

import org.slf4j.Logger;

import com.google.inject.Inject;

import com.cathive.fx.guice.GuiceFXMLLoader;

import com.bombasticoctocat.bomberman.game.Particle;

public class ParticlesImagesManager {
    @Inject private GuiceFXMLLoader fxmlLoader;
    @InjectLog private static Logger log;

    private int imagesLeftToRender = 0;
    private double scale = 0.0;
    private double refreshToScale = 0.0;
    private Runnable onRefreshcCompleteCallback;

    private class ParticleInformation {
        private final Group node;
        private final int width, height;
        private WritableImage image;


        public WritableImage getImage() {
            return image;
        }

        ParticleInformation(Group node, int width, int height) {
            this.node = node;
            this.width = width;
            this.height = height;
            if (scale > 0) refresh();
        }

        void refresh() {
            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            parameters.setTransform(new Scale(
                    (width * scale) / node.prefWidth(0.0),
                    (height * scale) / node.prefHeight(0.0)));

            ++imagesLeftToRender;
            node.snapshot(param -> {
                image = param.getImage();
                --imagesLeftToRender;
                if (imagesLeftToRender == 0) {
                    if (onRefreshcCompleteCallback != null) {
                        Platform.runLater(onRefreshcCompleteCallback);
                    }
                    if (refreshToScale != 0.0) {
                        double newScale = refreshToScale;
                        refreshToScale = 0.0;
                        refreshParticlesImages(newScale);
                    }
                }
                return null;
            }, parameters, new WritableImage((int)(width * scale) + 1, (int)(height * scale) + 1));
        }
    }

    private final HashMap<String, ParticleInformation> loadedParticles = new HashMap<>();

    public void refreshParticlesImages(double newScale) {
        if (newScale <= 0 || newScale == scale) {
            return;
        }

        if (imagesLeftToRender == 0) {
            scale = newScale;

            for (Map.Entry<String, ParticleInformation> particle : loadedParticles.entrySet()) {
                particle.getValue().refresh();
            }
        } else {
            refreshToScale = newScale;
            log.debug("Delayed refresh");
        }
    }

    private GuiceFXMLLoader.Result loadFxmlParticle(String particleName) {
        try {
            URL resUrl = getClass().getResource("fxml/tiles/" + particleName + ".fxml");
            if (resUrl == null) {
                throw new RuntimeException("Could not find: fxml/tiles/" + particleName + ".fxml");
            }
            return fxmlLoader.load(resUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public WritableImage getParticleImage(String particleName, Particle particle) {
        if (!loadedParticles.containsKey(particleName)) {
            Group particleNode = loadFxmlParticle(particleName).getRoot();
            ParticleInformation particleInfo = new ParticleInformation(particleNode, particle.width(), particle.height());
            loadedParticles.put(particleName, particleInfo);
        }
        return loadedParticles.get(particleName).getImage();
    }

    public void setOnRefreshCompleteHandler(Runnable callback) {
        onRefreshcCompleteCallback = callback;
    }

    public boolean isRefreshing() {
        return imagesLeftToRender != 0;
    }
}
