package ch.zhaw.it.pm.humanangeryourself.ui.view;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerSetup;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class StartScreenViewLoader {
    /**
     * Name of the FXML file, located next to this class.
     */
    private static final String FXML_NAME = "StartScreenView.fxml";

    /**
     * Controller created by the {@link FXMLLoader}
     */
    private final StartScreenViewController controller;

    /**
     * Scene containing the loaded start screen.
     */
    private final Scene scene;


    /**
     * Loads the start screen and creates the scene for it.
     *
     * @param onStart called with the entered players when the game is started
     * @throws NullPointerException  if {@code onStart} is {@code null}
     * @throws IllegalStateException if the FXML file cannot be loaded
     */
    public StartScreenViewLoader(Consumer<List<PlayerSetup>> onStart) {
        Objects.requireNonNull(onStart, "onStart must not be null");
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(FXML_NAME)));
            Parent rootNode = loader.load();
            controller = loader.getController();
            controller.setOnGameStart(onStart);
            scene = new Scene(rootNode);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load the start screen.", e);
        }
    }


    /**
     * Returns the controller of the main view.
     *
     * @return the main view controller
     */
    public StartScreenViewController getController() {
        return controller;
    }

    /**
     * Returns the scene containing the main view.
     *
     * @return the scene of the game screen
     */
    public Scene getScene() {
        return scene;
    }
}
