package com.bombasticoctocat.bomberman;

import javafx.event.ActionEvent;
import org.slf4j.Logger;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements ViewController {
    @InjectLog private static Logger log;

    @Override public void enteredView() {}
    @Override public void leavedView() {}
    @Override public void initialize(URL location, ResourceBundle resources) {}

    @FXML
    private void handleGitHubClick(ActionEvent actionEvent){
        log.debug("clicked hyperlink");
        Bomberman.openPage("https://github.com/BombasticOctocat/Bomberman");
    }
}
