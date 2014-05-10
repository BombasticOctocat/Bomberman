package com.bombasticoctocat.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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

    private double scale = 0.0;

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

            node.snapshot(param -> {
                image = param.getImage();
                return null;
            }, parameters, new WritableImage((int)(width * scale), (int)(height * scale)));
        }
    }

    private final HashMap<String, ParticleInformation> loadedParticles = new HashMap<>();

    public void refreshParticlesImages(double newScale) {
        if (newScale <= 0 || newScale == scale) {
            return;
        }

        scale = newScale;

        for (Map.Entry<String, ParticleInformation> particle: loadedParticles.entrySet()) {
            particle.getValue().refresh();
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
}
