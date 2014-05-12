package com.bombasticoctocat.bomberman;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;

public class SettingsController {

    @InjectLog Logger log;
    @FXML private Button UpSettingBtn, DownSettingBtn, LeftSettingBtn, RightSettingBtn,
                         SetBombSettingBtn, RestoreDefaultSettingBtn, PauseSettingBtn;
    //private String upB, downB, leftB, rightB, setB, defaultB, pauseB;

    private void handleKeyEvent(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            Button src = (Button)event.getSource();
            log.info(src.getId());
            String newSetting = event.getCode().toString();
            log.info(newSetting);
            switch(src.getId()){

                case "UpSettingBtn":
                    UpSettingBtn.setText(newSetting);
                    break;

                case "DownSettingBtn":
                    DownSettingBtn.setText(newSetting);
                    break;

                case "LeftSettingBtn":
                    LeftSettingBtn.setText(newSetting);
                    break;

                case "RightSettingBtn":
                    RightSettingBtn.setText(newSetting);
                    break;
                case "SetBombSettingBtn":
                    SetBombSettingBtn.setText(newSetting);
                    break;

                case "PauseSettingBtn":
                    PauseSettingBtn.setText(newSetting);
                    break;

                default:
                    break;
            }
        }
    }

    @FXML
    private void handleUpSettingClick(ActionEvent actionEvent) throws IOException {
        log.info("Clicked 'Up");
        UpSettingBtn.setText("...");
        UpSettingBtn.setOnKeyPressed(this::handleKeyEvent);
    }

    @FXML
    private void handleDownSettingClick(ActionEvent actionEvent) throws IOException {
        log.info("Clicked 'Down'");
        DownSettingBtn.setText("...");
        DownSettingBtn.setOnKeyPressed(this::handleKeyEvent);

    }

    @FXML
    private void handleLeftSettingClick(ActionEvent actionEvent) throws IOException {
        log.info("Clicked 'Left'");
        LeftSettingBtn.setOnKeyPressed(this::handleKeyEvent);
        LeftSettingBtn.setText("...");
    }

    @FXML
    private void handleRightSettingClick(ActionEvent actionEvent) throws IOException {
        log.info("Clicked 'Right'");
        RightSettingBtn.setOnKeyPressed(this::handleKeyEvent);
        RightSettingBtn.setText("...");
    }

    @FXML
    private void handleSetBombSettingClick(ActionEvent actionEvent) throws IOException {
        log.info("Clicked 'Bomb'");
        SetBombSettingBtn.setOnKeyPressed(this::handleKeyEvent);
        SetBombSettingBtn.setText("...");
    }

    @FXML
    private void handleRestoreDefaultSettingClick(ActionEvent actionEvent) throws IOException {
        log.info("Clicked 'Default");
        UpSettingBtn.setText("UP");
        DownSettingBtn.setText("DOWN");
        LeftSettingBtn.setText("LEFT");
        RightSettingBtn.setText("RIGHT");
        SetBombSettingBtn.setText("Z");
        PauseSettingBtn.setText("P");
    }

    @FXML
    private void handlePauseSettingClick(ActionEvent actionEvent) throws IOException {
        log.info("Clicked 'Pause'");
        PauseSettingBtn.setOnKeyPressed(this::handleKeyEvent);
    }
}
