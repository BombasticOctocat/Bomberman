package com.bombasticoctocat.bomberman;

import javafx.event.ActionEvent;
import org.slf4j.Logger;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutController implements ViewController {
    @InjectLog Logger log;

    @Override public void enteredView() {}
    @Override public void leavedView() {}
    @Override public void initialize(URL location, ResourceBundle resources) {}

    @FXML void handleGitHubClick(ActionEvent actionEvent){
        try {
            URI uri = new URI("https://github.com/BombasticOctocat/Bomberman");
            openPage(uri);
        }
        catch (URISyntaxException e) {
            throw new RuntimeException("Incorrect URI", e);
        }
    }

    public static void openPage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                throw new RuntimeException("Couldn't open webPage", e);
            }
        }
    }
}
