package ch.zhaw.it.pm.humanangeryourself;

import javafx.application.Application;
import javafx.stage.Stage;

import ch.zhaw.it.pm.humanangeryourself.ui.view.BoardViewLoader;

public class App extends Application {
    public static final int DESIRED_SIZE = 700;

    private BoardViewLoader boardViewLoader;

    /**
     * Creates a new instance.
     */
    public App() {
    }

    public static void main(String[] args) {
        Application.launch(App.class);
    }

    /**
     * Initialization method for the JavaFX application.
     */
    @Override
    public void init() {
        // TODO: maybe move into start:
        //  "Maybe a bit much for init? the loader allready creates a scene in the init,
        //  which normally should be made in start."
        boardViewLoader = new BoardViewLoader(DESIRED_SIZE);
    }

    /**
     * Start method for the JavaFX application.
     *
     * @param primaryStage the primary stage for this application, onto which the application scene can be set
     */
    @Override
    public void start(Stage primaryStage) {
        openMainWindow(primaryStage);
        primaryStage.setTitle("Human Anger Yourself");
    }

    private void openMainWindow(Stage stage) {
        stage.setScene(boardViewLoader.getScene());
        stage.show();
    }
}
