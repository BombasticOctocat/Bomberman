package com.bombasticoctocat.bomberman;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import org.slf4j.Logger;

import com.google.inject.Inject;

import com.cathive.fx.guice.GuiceFXMLLoader;

public class MainController {
    @FXML private AnchorPane windowPane;
    @InjectLog Logger log;
    @Inject private GuiceFXMLLoader fxmlLoader;

    Node loadView(String name) throws IOException {
        Node node = fxmlLoader.load(getClass().getResource("fxml/" + name + ".fxml")).getRoot();
        windowPane.getChildren().setAll(node);
        return node;
    }

    @FXML
    private void handleNewClick(ActionEvent actionEvent) throws IOException {
        log.info("Clicked 'New'");
        loadView("game");
    }

    @FXML
    private void handleExitClick(ActionEvent actionEvent) throws IOException  {
        log.info("Clicked 'Exit'");
        Bomberman.handleExitEvent();
    }

    @FXML
    private void handleSettingsClick(ActionEvent actionEvent) throws IOException  {
        log.info("Clicked 'Settings'");
        loadView("settings");
    }

    @FXML
    private void handleAboutClick(ActionEvent actionEvent) throws IOException  {
        log.info("Clicked 'About'");
        loadView("about");
    }
}
