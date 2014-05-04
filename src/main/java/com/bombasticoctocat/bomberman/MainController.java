package com.bombasticoctocat.bomberman;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

public class MainController {
    @FXML private Pane windowPane;

    @FXML
    private void handleNewClick(ActionEvent actionEvent) {
    }

    @FXML
    private void handleExitClick(ActionEvent actionEvent) {
        Action response = Dialogs.create()
            .owner(null)
            .masthead(null)
            .nativeTitleBar()
            .title("Are you sure?")
            .message("Are you sure you want to exit application?")
            .showConfirm();

        if (response == Dialog.Actions.YES) {
            Platform.exit();
        }
    }

    @FXML
    private void handleSettingsClick(ActionEvent actionEvent) {
    }

    @FXML
    private void handleAboutClick(ActionEvent actionEvent) {
    }
}
