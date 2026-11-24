package ch.zhaw.it.pm.humanangeryourself.ui.view;

import ch.zhaw.it.pm.humanangeryourself.ui.viewmodel.GameViewModel;

import javafx.fxml.FXML;

/**
 * Controller for the game screen which combines the board and player views.
 */
public class MainViewController {
    /** Controller of the included board view, injected by the FXML loader. */
    @FXML
    private BoardViewController boardViewController;

    /** Controller of the included player view, injected by the FXML loader. */
    @FXML
    private PlayerViewController playerViewController;

    /**
     * Creates the controller. Called by the FXML loader.
     */
    public MainViewController() {
    }

    /**
     * Connects both sub views to the given view model.
     *
     * @param viewModel the view model of the game to display
     */
    public void setViewModel(GameViewModel viewModel) {
        boardViewController.setViewModel(viewModel);
        playerViewController.setViewModel(viewModel);
    }
}
