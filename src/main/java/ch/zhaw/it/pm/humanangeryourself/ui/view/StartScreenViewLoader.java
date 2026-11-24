package ch.zhaw.it.pm.humanangeryourself.ui.view;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerSetup;

import java.util.List;
import java.util.function.Consumer;

/**
 * Loads the start screen, where the players enter their names.
 */
public class StartScreenViewLoader extends ViewLoader<StartScreenViewController> {
    /** Name of the FXML file, located next to this class. */
    private static final String FXML_NAME = "StartScreenView.fxml";

    /**
     * Loads the start screen and registers the action that starts the game.
     *
     * @param onStart called with the entered players when the game is started
     * @throws NullPointerException  if {@code onStart} is {@code null}
     * @throws IllegalStateException if the FXML file cannot be loaded
     */
    public StartScreenViewLoader(Consumer<List<PlayerSetup>> onStart) {
        super(FXML_NAME);
        getController().setOnGameStart(onStart);
    }
}
