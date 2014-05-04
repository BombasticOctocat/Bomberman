package com.bombasticoctocat.bomberman;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bomberman extends Application {
    private static final Logger log = LoggerFactory.getLogger(Bomberman.class);

    @Override
    public void start(Stage primaryStage) throws Exception{
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
