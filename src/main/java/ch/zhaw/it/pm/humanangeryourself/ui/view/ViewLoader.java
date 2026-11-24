package ch.zhaw.it.pm.humanangeryourself.ui.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Loads an FXML view located next to this class and wraps it in a {@link Scene}
 * that uses the application stylesheet.
 *
 * @param <C> type of the controller declared in the FXML file
 */
public abstract class ViewLoader<C> {
    /** Stylesheet applied to every scene, located next to this class. */
    private static final String STYLESHEET_NAME = "application.css";

    /** Controller created by the {@link FXMLLoader}. */
    private final C controller;

    /** Scene containing the loaded view. */
    private final Scene scene;

    /**
     * Loads the given FXML file and creates the scene for it.
     *
     * @param fxmlName name of the FXML file, located next to this class
     * @throws IllegalStateException if the FXML file cannot be loaded
     */
    protected ViewLoader(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(resource(fxmlName));
            Parent rootNode = loader.load();
            controller = loader.getController();
            scene = new Scene(rootNode);
            scene.getStylesheets().add(resource(STYLESHEET_NAME).toExternalForm());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load the view " + fxmlName, e);
        }
    }

    /**
     * Returns the controller of the loaded view.
     *
     * @return the controller declared in the FXML file
     */
    public C getController() {
        return controller;
    }

    /**
     * Returns the scene containing the loaded view.
     *
     * @return the scene of the view
     */
    public Scene getScene() {
        return scene;
    }

    private static URL resource(String name) {
        return Objects.requireNonNull(ViewLoader.class.getResource(name), "Missing resource: " + name);
    }
}
