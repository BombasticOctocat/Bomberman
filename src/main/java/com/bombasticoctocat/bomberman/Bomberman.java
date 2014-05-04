package com.bombasticoctocat.bomberman;

import java.util.List;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Module;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;

public class Bomberman extends GuiceApplication {
    @InjectLog private Logger log;
    @Inject private GuiceFXMLLoader fxmlLoader;

    @Override
    public void start(Stage primaryStage) throws Exception {
        log.info("Started application");

        Scene root = new Scene(fxmlLoader.load(getClass().getResource("main.fxml")).getRoot());
        primaryStage.setScene(root);
        primaryStage.setTitle("Bomberman");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init(List<Module> modules) throws Exception {
        modules.add(new BombermanModule());
    }
}
