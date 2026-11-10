package ch.zhaw.it.pm.humanangeryourself.ui.view;

import ch.zhaw.it.pm.humanangeryourself.ui.viewmodel.GameViewModel;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Objects;

/**
 * Loads the complete game screen.
 */
public class MainViewLoader {
    /** Name of the FXML file, located next to this class. */
    private static final String FXML_NAME = "MainView.fxml";

    /** Controller created by the {@link FXMLLoader}. */
    private final MainViewController controller;

    /** Scene containing the loaded game screen. */
    private final Scene scene;

    /**
     * Loads the main view and creates the scene for it.
     *
     * @param viewModel the view model of the game, not used yet
     * @throws IllegalStateException if the FXML file cannot be loaded
     */
    public MainViewLoader(GameViewModel viewModel) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(FXML_NAME)));
            Pane rootNode = loader.load();
            controller = loader.getController();
            controller.setViewModel(viewModel);
            scene = new Scene(rootNode);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load the main game view.", e);
        }
    }

    /**
     * Returns the controller of the main view.
     *
     * @return the main view controller
     */
    public MainViewController getController() {
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
