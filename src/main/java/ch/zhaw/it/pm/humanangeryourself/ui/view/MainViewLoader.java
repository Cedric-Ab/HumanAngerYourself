package ch.zhaw.it.pm.humanangeryourself.ui.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Objects;

/**
 * Loads the complete game screen.
 */
public class MainViewLoader {
    private static final String FXML_NAME = "MainView.fxml";

    private final MainViewController controller;
    private final Scene scene;

    public MainViewLoader() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(FXML_NAME)));
            Pane rootNode = loader.load();

            controller = loader.getController();
            scene = new Scene(rootNode);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load the main game view.", e);
        }
    }

    public MainViewController getController() {
        return controller;
    }

    public Scene getScene() {
        return scene;
    }
}
