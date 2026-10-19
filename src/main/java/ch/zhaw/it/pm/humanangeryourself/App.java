package ch.zhaw.it.pm.humanangeryourself;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {
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
     * <p>
     * Opens the main window and sets the study data in the main presenter.
     *
     * @param primaryStage the primary stage for this application, onto which the application scene can be set
     */
    @Override
    public void start(Stage primaryStage) {
        openMainWindow(primaryStage);
        primaryStage.setTitle("EasyGrade");
    }

    private void openMainWindow(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/view/BoardView.fxml"));
            Pane rootNode = loader.load();

            loader.getController();

            Scene scene = new Scene(rootNode);

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
