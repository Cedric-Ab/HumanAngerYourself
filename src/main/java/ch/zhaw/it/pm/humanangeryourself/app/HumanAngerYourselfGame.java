package ch.zhaw.it.pm.humanangeryourself.app;

import ch.zhaw.it.pm.humanangeryourself.domain.game.GameController;
import ch.zhaw.it.pm.humanangeryourself.service.LocalGameService;
import ch.zhaw.it.pm.humanangeryourself.api.GameService;

import ch.zhaw.it.pm.humanangeryourself.ui.viewmodel.GameViewModel;
import ch.zhaw.it.pm.humanangeryourself.ui.view.MainViewLoader;

import javafx.application.Application;
import javafx.stage.Stage;

public class HumanAngerYourselfGame extends Application {
    private MainViewLoader mainViewLoader;

    /**
     * Creates a new instance.
     */
    public HumanAngerYourselfGame() {
    }

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

    private void openMainWindow(Stage stage) {
        stage.setScene(mainViewLoader.getScene());
        stage.show();
    }
}
