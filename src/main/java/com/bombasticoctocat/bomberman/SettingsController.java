package com.bombasticoctocat.bomberman;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;

public class SettingsController implements ViewController {
    private @InjectLog Logger log;
    private @FXML GridPane settingsPane;
    private SettingButton currentWaitingButton;

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
        currentWaitingButton = null;
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
        if (currentWaitingButton == null) {
            return;
        }

        Settings.Key key = currentWaitingButton.getSettingKey();
        if (event.getCode() != KeyCode.ESCAPE) {
            boolean isInUse = false;
            for (SettingButton btn: getSettingButtons()) {
                Settings.Key settingKey = btn.getSettingKey();
                KeyCode keyCode = settingKey.getSetting();
                if(keyCode == event.getCode()){
                    isInUse = true;
                    break;
                }
            }
            if(!isInUse)
                key.setSetting(event.getCode());
        }
        currentWaitingButton.setText(key.getSetting().toString());
        currentWaitingButton = null;
    }

    @FXML
    private void handleButtonClick(ActionEvent actionEvent) {
        SettingButton button = (SettingButton) actionEvent.getSource();
        if(currentWaitingButton != null)
            return;
        currentWaitingButton = button;

        button.setText("...");
        settingsPane.requestFocus();
    }

    @FXML
    private void handleRestoreDefaultSettingClick(ActionEvent actionEvent) {
        settingsPane.requestFocus();
        for (SettingButton button: getSettingButtons()) {
            Settings.Key key = button.getSettingKey();
            key.setSetting(key.getDefaultSetting());
            button.setText(key.getSetting().toString());
        }
    }
}
