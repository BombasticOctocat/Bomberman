package com.bombasticoctocat.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;

public class SettingsController implements ViewController {
    private @InjectLog Logger log;
    private @FXML GridPane settingsPane;
    private String nameCurrentWaitingSetting;
    private final String BTN_ID_SUFFIX = "SettingBtn";

    private Settings.Key getKey(Button btn) {
        String id = btn.getId();
        if (!id.matches("^[a-z]+" + BTN_ID_SUFFIX + "$")) {
            throw new RuntimeException("Incorrect button id");
        }
        String name = id.replaceAll(BTN_ID_SUFFIX + "$", "").toUpperCase();
        return Settings.Key.valueOf(name);
    }

    @Override
    public void enteredView() {
        nameCurrentWaitingSetting = null;
        settingsPane.getScene().setOnKeyPressed(this::handleKeyPress);
        settingsPane.requestFocus();
    }

    @Override
    public void leavedView() {
        settingsPane.getScene().setOnKeyPressed(null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private void handleKeyPress(KeyEvent event) {
        if (nameCurrentWaitingSetting == null) {
            return;
        }

        Button button = (Button) settingsPane.lookup("#" + nameCurrentWaitingSetting);
        nameCurrentWaitingSetting = null;
        Settings.Key key = getKey(button);
        if (event.getCode() != KeyCode.ESCAPE) {
            key.setSetting(event.getCode());
        }
        button.setText(key.getSetting().toString());
    }

    @FXML void handleButtonClick(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        nameCurrentWaitingSetting = button.getId();
        button.setText("...");
        settingsPane.requestFocus();
    }

    @FXML
    private void handleRestoreDefaultSettingClick(ActionEvent actionEvent) {
        log.info("Clicked 'Default");
        for (Settings.Key key: Settings.Key.values()) {
            Button button = (Button) settingsPane.lookup("#" + key.toString().toLowerCase() + BTN_ID_SUFFIX);
            key.setSetting(key.getDefaultSetting());
            button.setText(key.getSetting().toString());
        }
    }
}
