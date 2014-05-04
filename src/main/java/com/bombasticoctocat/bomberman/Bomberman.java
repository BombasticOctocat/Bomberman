package com.bombasticoctocat.bomberman;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;

public class Bomberman extends Application {
    @InjectLog Logger log;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Injector injector = Guice.createInjector(new BombermanModule());
        injector.injectMembers(this);

        log.info("Started application");

        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setScene(new Scene(root, 640, 480));
        primaryStage.setTitle("Bomberman");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
