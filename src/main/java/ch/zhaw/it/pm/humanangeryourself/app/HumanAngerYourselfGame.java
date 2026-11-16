package ch.zhaw.it.pm.humanangeryourself.app;

import ch.zhaw.it.pm.humanangeryourself.domain.game.GameController;
import ch.zhaw.it.pm.humanangeryourself.service.LocalGameService;
import ch.zhaw.it.pm.humanangeryourself.api.GameService;

import ch.zhaw.it.pm.humanangeryourself.ui.viewmodel.GameViewModel;
import ch.zhaw.it.pm.humanangeryourself.ui.view.MainViewLoader;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX application of the game "Human Anger Yourself".
 * It wires up the game controller, the service and the view model and opens the main window.
 */
public class HumanAngerYourselfGame extends Application {
    /** Loader of the main game screen, created in {@link #start(Stage)}. */
    private MainViewLoader mainViewLoader;

    /**
     * Creates a new instance.
     */
    public HumanAngerYourselfGame() {
    }

    /**
     * Entry point of the application. Launches the JavaFX application.
     *
     * @param args command line arguments, not used
     */
    public static void main(String[] args) {
        Application.launch(HumanAngerYourselfGame.class);
    }

    /**
     * Start method for the JavaFX application.
     *
     * @param primaryStage the primary stage for this application, onto which the application scene can be set
     */
    @Override
    public void start(Stage primaryStage) {
        GameController controller = new GameController();
        GameService service = new LocalGameService(controller);
        GameViewModel viewModel = new GameViewModel(service);

        mainViewLoader = new MainViewLoader(viewModel);
        openMainWindow(primaryStage);

        primaryStage.setTitle("Human Anger Yourself");
    }

    /**
     * Shows the main game screen on the given stage.
     *
     * @param stage the stage to show the main screen on
     */
    private void openMainWindow(Stage stage) {
        stage.setScene(mainViewLoader.getScene());
        stage.show();
    }
}
