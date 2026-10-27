package ch.zhaw.it.pm.humanangeryourself;

import javafx.application.Application;
import javafx.stage.Stage;

import ch.zhaw.it.pm.humanangeryourself.ui.view.MainViewLoader;

public class App extends Application {
    private MainViewLoader mainViewLoader;

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
    }

    /**
     * Start method for the JavaFX application.
     *
     * @param primaryStage the primary stage for this application, onto which the application scene can be set
     */
    @Override
    public void start(Stage primaryStage) {
        mainViewLoader = new MainViewLoader();
        openMainWindow(primaryStage);
        primaryStage.setTitle("Human Anger Yourself");
    }

    private void openMainWindow(Stage stage) {
        stage.setScene(mainViewLoader.getScene());
        stage.show();
    }
}
