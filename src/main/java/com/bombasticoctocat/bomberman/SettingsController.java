package com.bombasticoctocat.bomberman;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;

public class SettingsController implements ViewController {
    private @InjectLog Logger log;
    private @FXML GridPane settingsPane;
    private String nameCurrentWaitingSetting;

    private List<SettingButton> getSettingButtons() {
        List<SettingButton> result = new ArrayList<>();
        Set<Node> buttons = settingsPane.lookupAll(".button");
        for (Node node: buttons) {
            if (node instanceof SettingButton) {
                result.add((SettingButton) node);
            }
        }
        return result;
    }

    @Override
    public void enteredView() {
        nameCurrentWaitingSetting = null;
        settingsPane.getScene().setOnKeyPressed(this::handleKeyPress);
        settingsPane.requestFocus();
        for (SettingButton button: getSettingButtons()) {
            button.setText(button.getSettingKey().getSetting().toString());
        }
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

        SettingButton button = (SettingButton) settingsPane.lookup("#" + nameCurrentWaitingSetting);
        nameCurrentWaitingSetting = null;
        Settings.Key key = button.getSettingKey();
        if (event.getCode() != KeyCode.ESCAPE) {
            key.setSetting(event.getCode());
        }
        button.setText(key.getSetting().toString());
    }

    @FXML
    void handleButtonClick(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        nameCurrentWaitingSetting = button.getId();
        button.setText("...");
        settingsPane.requestFocus();
    }

    @FXML
    private void handleRestoreDefaultSettingClick(ActionEvent actionEvent) {
        log.info("Clicked 'Default");
        for (SettingButton button: getSettingButtons()) {
            Settings.Key key = button.getSettingKey();
            key.setSetting(key.getDefaultSetting());
            button.setText(key.getSetting().toString());
        }
    }
}
