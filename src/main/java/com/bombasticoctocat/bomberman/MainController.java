package com.bombasticoctocat.bomberman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class MainController {
    @FXML private Pane windowPane;

    @FXML
    private void handleMenuAction(ActionEvent event) {
        System.out.println("You clicked me!");
    }
}
