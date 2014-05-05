package com.bombasticoctocat.bomberman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import org.slf4j.Logger;

public class MainController {
    @FXML private Pane windowPane;
    @InjectLog Logger log;

    @FXML
    private void handleNewClick(ActionEvent actionEvent) {
        log.info("Clicked 'New'");
    }

    @FXML
    private void handleExitClick(ActionEvent actionEvent) {
        log.info("Clicked 'Exit'");
        Bomberman.handleExitEvent();
    }

    @FXML
    private void handleSettingsClick(ActionEvent actionEvent) {
        log.info("Clicked 'Settings'");
    }

    @FXML
    private void handleAboutClick(ActionEvent actionEvent) {
        log.info("Clicked 'About'");
    }
}
