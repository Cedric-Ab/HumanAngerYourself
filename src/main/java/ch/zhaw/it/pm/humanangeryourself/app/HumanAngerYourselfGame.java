package ch.zhaw.it.pm.humanangeryourself.app;

import javafx.application.Application;
import javafx.stage.Stage;

import ch.zhaw.it.pm.humanangeryourself.api.GameService;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerSetup;
import ch.zhaw.it.pm.humanangeryourself.config.GameConfig;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.game.GameBuilder;
import ch.zhaw.it.pm.humanangeryourself.domain.game.GameController;
import ch.zhaw.it.pm.humanangeryourself.service.LocalGameService;
import ch.zhaw.it.pm.humanangeryourself.ui.view.MainViewLoader;
import ch.zhaw.it.pm.humanangeryourself.ui.view.StartScreenViewLoader;
import ch.zhaw.it.pm.humanangeryourself.ui.viewmodel.GameViewModel;

import java.util.List;

/**
 * JavaFX application of the game "Human Anger Yourself".
 * It wires up the game controller, the service and the view model and opens the main window.
 */
public class HumanAngerYourselfGame extends Application {
    /**
     * Loader of the main game screen, created in {@link #start(Stage)}.
     */
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
     * @param stage the primary stage for this application, onto which the application scene can be set
     */
    @Override
    public void start(Stage stage) {
        GameConfig config = GameConfig.defaults();
        StartScreenViewLoader startScreen = new StartScreenViewLoader(playerSetups -> {
            showGame(stage, createViewModel(config, playerSetups));
        });

        stage.setTitle("Human Anger Yourself");
        stage.setScene(startScreen.getScene());
        stage.show();





        /*
        GameController controller = new GameController();
        GameService service = new LocalGameService(controller);
        GameViewModel viewModel = new GameViewModel(service);
        mainViewLoader = new MainViewLoader(viewModel);
        openMainWindow(primaryStage);

        primaryStage.setTitle("Human Anger Yourself");*/
    }

    /**
     * Creates a new game for the given players and connects it to a view model.
     *
     * @param config       settings of the game
     * @param playerSetups name and color of every player
     * @return the view model of the new game
     */
    private GameViewModel createViewModel(GameConfig config, List<PlayerSetup> playerSetups) {
        Game game = new GameBuilder()
                .setGameConfig(config)
                .setPlayerSetups(playerSetups)
                .createGame();
        GameService service = new LocalGameService(new GameController(game));
        return new GameViewModel(service);
    }

    /**
     * Shows the game screen for the given view model.
     *
     * @param stage     the stage to show the game screen on
     * @param viewModel the view model of the game
     */
    private void showGame(Stage stage, GameViewModel viewModel) {
        stage.setScene(new MainViewLoader(viewModel).getScene());
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
