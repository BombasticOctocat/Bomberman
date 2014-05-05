package com.bombasticoctocat.bomberman;

import java.util.List;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;

public class Bomberman extends GuiceApplication {
    @InjectLog private static Logger log;
    @Inject private GuiceFXMLLoader fxmlLoader;

    @Override
    public void start(Stage primaryStage) throws Exception {
        log.info("Started application");

            Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
                Platform.runLater(() -> {
                    Dialogs.create()
                        .nativeTitleBar()
                        .owner(null)
                        .masthead(null)
                        .title("Unexpected Exception")
                        .showException(e);

                    Platform.exit();
                });
            });

        Platform.setImplicitExit(false);
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            Bomberman.handleExitEvent();
        });

        Scene root = new Scene(fxmlLoader.load(getClass().getResource("main.fxml")).getRoot());
        primaryStage.setScene(root);
        primaryStage.setTitle("Bomberman");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void handleExitEvent() {
        Action response = Dialogs.create()
            .owner(null)
            .masthead(null)
            .nativeTitleBar()
            .title("Are you sure?")
            .message("Are you sure you want to exit application?")
            .showConfirm();

        if (response == Dialog.Actions.YES) {
            log.info("Exiting application");
            Platform.exit();
        }
    }

    @Override
    public void init(List<Module> modules) throws Exception {
        modules.add(new BombermanModule());
    }
}

class BombermanModule extends AbstractModule {
    @Override
    protected void configure() {
        bindListener(Matchers.any(), new Slf4jLoggerTypeListener());
    }
}
