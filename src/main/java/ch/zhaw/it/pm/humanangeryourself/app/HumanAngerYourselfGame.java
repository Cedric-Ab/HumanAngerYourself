package ch.zhaw.it.pm.humanangeryourself.app;

import ch.zhaw.it.pm.humanangeryourself.service.LocalGameService;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class HumanAngerYourselfGame extends Application {
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
        LocalGameService service = new LocalGameService();

        openStartScreen(primaryStage);
    }

    private void openStartScreen(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/zhaw/it/pm/humanangeryourself/ui/view/StartScreenView.fxml"));
            Pane rootNode = loader.load();

            Scene scene = new Scene(rootNode);

            stage.setScene(scene);
            stage.show();

        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }
}
