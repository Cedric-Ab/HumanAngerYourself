package ch.zhaw.it.pm.humanangeryourself.ui.view;

import ch.zhaw.it.pm.humanangeryourself.ui.viewmodel.GameViewModel;

/**
 * Loads the game screen, which combines the board and the player view.
 */
public class MainViewLoader extends ViewLoader<MainViewController> {
    /** Name of the FXML file, located next to this class. */
    private static final String FXML_NAME = "MainView.fxml";

    /**
     * Loads the game screen and connects it to the given view model.
     *
     * @param viewModel the view model of the game to display
     * @throws IllegalStateException if the FXML file cannot be loaded
     */
    public MainViewLoader(GameViewModel viewModel) {
        super(FXML_NAME);
        getController().setViewModel(viewModel);
    }
}
