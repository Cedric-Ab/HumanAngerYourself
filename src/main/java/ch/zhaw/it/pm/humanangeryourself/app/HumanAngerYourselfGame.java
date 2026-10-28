package ch.zhaw.it.pm.humanangeryourself.app;


import javafx.application.Application;
import javafx.stage.Stage;

import ch.zhaw.it.pm.humanangeryourself.service.LocalGameService;
import ch.zhaw.it.pm.humanangeryourself.ui.view.MainViewLoader;

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
        // TODO composition root, add the config, game, viewmodel classes etc.
        // TODO change type to interface later on, pass the game to localgameservice
        // TODO padd the viewmodel to the controller
        new LocalGameService();

        mainViewLoader = new MainViewLoader();
        openMainWindow(primaryStage);
        primaryStage.setTitle("Human Anger Yourself");
    }

    private void openMainWindow(Stage stage) {
        stage.setScene(mainViewLoader.getScene());
        stage.show();
    }
}
