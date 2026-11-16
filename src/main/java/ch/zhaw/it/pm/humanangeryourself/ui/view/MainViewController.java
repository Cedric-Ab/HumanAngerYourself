package ch.zhaw.it.pm.humanangeryourself.ui.view;

import ch.zhaw.it.pm.humanangeryourself.ui.viewmodel.GameViewModel;

import javafx.fxml.FXML;

/**
 * Controller for the game screen which combines the board and player views.
 */
public class MainViewController {
    @FXML
    private BoardViewController boardViewController;
    @FXML
    private PlayerViewController playerViewController;

    public void setViewModel(GameViewModel viewModel) {
        boardViewController.setViewModel(viewModel);
        playerViewController.setViewModel(viewModel);
    }
}
