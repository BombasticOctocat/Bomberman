package com.bombasticoctocat.bomberman;

/**
 * Created by Kamil on 2014-05-11.
 */

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.slf4j.Logger;


public class SettingsController {

    @InjectLog Logger log;

    @FXML
    private void handleUpSettingClick(ActionEvent actionEvent) throws IOException {
        log.info("Clicked 'Up");
    }

    @FXML
    private void handleDownSettingClick(ActionEvent actionEvent) throws IOException {
        log.info("Clicked 'Down'");
    }

    @FXML
    private void handleLeftSettingClick(ActionEvent actionEvent) throws IOException {
        log.info("Clicked 'Left'");
    }

    @FXML
    private void handleRightSettingClick(ActionEvent actionEvent) throws IOException {
        log.info("Clicked 'Right'");
    }

    @FXML
    private void handleSetBombSettingClick(ActionEvent actionEvent) throws IOException {
        log.info("Clicked 'Bomb'");
    }

    @FXML
    private void handleRestoreDefaultSettingClick(ActionEvent actionEvent) throws IOException {
        log.info("Clocked 'Default'");
    }
}
