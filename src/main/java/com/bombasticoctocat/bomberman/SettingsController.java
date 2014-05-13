package com.bombasticoctocat.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;

public class SettingsController implements ViewController {
    @InjectLog Logger log;
    @FXML GridPane settingsPane;
    private String nameCurrentWaitingSetting;

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
        button.setText(event.getCode().toString());
    }

    @FXML void handleButtonClick(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        nameCurrentWaitingSetting = button.getId();
        button.setText("...");
        settingsPane.requestFocus();
    }

    @FXML
    private void handleRestoreDefaultSettingClick(ActionEvent actionEvent) throws IOException {
        log.info("Clicked 'Default");
        /*UpSettingBtn.setText("UP");
        DownSettingBtn.setText("DOWN");
        LeftSettingBtn.setText("LEFT");
        RightSettingBtn.setText("RIGHT");
        SetBombSettingBtn.setText("Z");
        PauseSettingBtn.setText("P");*/
    }
}
