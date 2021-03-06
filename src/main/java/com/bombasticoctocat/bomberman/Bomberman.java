package com.bombasticoctocat.bomberman;

import java.util.List;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.*;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import org.slf4j.Logger;

import java.io.InputStream;

import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;
import java.util.ArrayList;

public class Bomberman extends GuiceApplication {
    @InjectLog private static Logger log;
    @Inject private GuiceFXMLLoader fxmlLoader;
    private static MainController mainController;
    private static HostServices hostServices;

    @Override
    public void start(Stage primaryStage) throws Exception {
        log.info("Started application");

        List<InputStream> iconList = new ArrayList<InputStream>();
        iconList.add(getClass().getResourceAsStream("images/icon16.png"));
        iconList.add(getClass().getResourceAsStream("images/icon32.png"));
        iconList.add(getClass().getResourceAsStream("images/icon64.png"));
        iconList.add(getClass().getResourceAsStream("images/icon128.png"));
        for(InputStream il : iconList)
            primaryStage.getIcons().add(new Image(il));

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
            Platform.runLater(() -> {
                Dialogs.create()
                    .nativeTitleBar()
                    .owner(null)
                    .masthead(null)
                    .title("Unexpected Exception")
                    .showException(e);

                Platform.exit();
                System.exit(1);
            });
        });

        Platform.setImplicitExit(false);
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            Bomberman.handleExitEvent();
        });

        hostServices = getHostServices();

        GuiceFXMLLoader.Result result = fxmlLoader.load(getClass().getResource("fxml/main.fxml"));
        mainController = result.getController();
        Pane root = result.getRoot();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Bomberman");
        primaryStage.setMinWidth(root.getMinWidth());
        primaryStage.setMinHeight(root.getMinHeight());
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
            if (mainController != null) {
                mainController.leaveViews();
            }
            log.info("Exiting application");
            Platform.exit();
            System.exit(0);
        }
    }

    public static void openPage(String uri) {
        if (hostServices != null) {
            hostServices.showDocument(uri);
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
        bind(SettingsManager.class).toInstance(new SettingsManager());
        bind(ParticlesImagesManager.class).toInstance(new ParticlesImagesManager());
        bind(MapImageManager.class).toInstance(new MapImageManager());
        bind(GameObjectsManager.class).toInstance(new GameObjectsManager());
        requestStaticInjection(Settings.class);
    }
}
