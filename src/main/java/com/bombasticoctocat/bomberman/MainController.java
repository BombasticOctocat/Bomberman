package com.bombasticoctocat.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.slf4j.Logger;

import com.google.inject.Inject;

import com.cathive.fx.guice.GuiceFXMLLoader;

public class MainController implements Initializable {
    @FXML private AnchorPane windowPane;
    @FXML private final BooleanProperty gameIsNotRunning = new SimpleBooleanProperty();
    @FXML private MenuItem restoreMenuItem;

    @Inject private GuiceFXMLLoader fxmlLoader;
    @InjectLog private Logger log;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
         /* I wanted to use bidirectional binding from FXML file
            and perform binding with gameIsNotRunning in FXML
            unfortunately this feature is not implemented in
            JavaFX 8 yet, so we need to do it manually. */
        restoreMenuItem.disableProperty().bindBidirectional(gameIsNotRunning);
        gameIsNotRunning.set(true);
    }

    private class ViewManager {
        private final HashMap<String, Node> loadedViews = new HashMap<>();
        private final HashMap<String, ViewController> viewControllers = new HashMap<>();
        private String currViewName;

        public String getCurrViewName() {
            return currViewName;
        }

        public Node getNode(String viewName) {
            return loadedViews.get(currViewName);
        }

        public ViewController getController(String viewName) {
            return viewControllers.get(viewName);
        }

        private GuiceFXMLLoader.Result loadFxml(String viewName) {
            try {
                URL resUrl = getClass().getResource("fxml/" + viewName + ".fxml");
                if (resUrl == null) {
                    throw new RuntimeException("Could not find: fxml/" + viewName + ".fxml");
                }
                return fxmlLoader.load(resUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void loadView(String viewName) {
            if (viewName.equals(currViewName)) {
                return;
            }

            if (!loadedViews.containsKey(viewName)) {
                GuiceFXMLLoader.Result fxml = loadFxml(viewName);
                loadedViews.put(viewName, fxml.getRoot());
                viewControllers.put(viewName, fxml.getController());
            }

            ViewController ctrl;
            if (currViewName != null && (ctrl = viewControllers.get(currViewName)) != null) {
                ctrl.leavedView();
            }
            currViewName = viewName;
            windowPane.getChildren().setAll(loadedViews.get(viewName));
            if ((ctrl = viewControllers.get(currViewName)) != null) {
                ctrl.enteredView();
            }
        }
    }
    private final ViewManager viewManager = new ViewManager();

    @FXML
    private void handleNewClick(ActionEvent actionEvent) {
        log.info("Clicked 'New'");
        if (gameIsNotRunning.get()) {
            viewManager.loadView("game");
            ((GameController) viewManager.getController("game")).startGame();
            gameIsNotRunning.set(false);
        } else {
            Action response = Dialogs.create()
                .owner(null)
                .masthead(null)
                .nativeTitleBar()
                .title("Are you sure?")
                .message("Are you sure you want to start new game while another is running?")
                .showConfirm();

            if (response == Dialog.Actions.YES) {
                viewManager.loadView("game");
                GameController gameController = (GameController) viewManager.getController("game");
                gameController.stopGame();
                gameController.startGame();
            }
        }
    }

    @FXML
    private void handleRestoreClick(ActionEvent actionEvent) {
        log.info("Clicked 'Restore'");
        viewManager.loadView("game");
    }

    @FXML
    private void handleExitClick(ActionEvent actionEvent) {
        log.info("Clicked 'Exit'");
        Bomberman.handleExitEvent();
    }

    @FXML
    private void handleSettingsClick(ActionEvent actionEvent) {
        log.info("Clicked 'Settings'");
        viewManager.loadView("settings");
    }

    @FXML
    private void handleAboutClick(ActionEvent actionEvent) {
        log.info("Clicked 'About'");
        viewManager.loadView("about");
    }
}
