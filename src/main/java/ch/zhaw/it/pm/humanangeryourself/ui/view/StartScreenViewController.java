package ch.zhaw.it.pm.humanangeryourself.ui.view;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.List;

public class StartScreenViewController {

    @FXML
    private TextField player1Field;

    @FXML
    private TextField player2Field;

    @FXML
    private TextField player3Field;

    @FXML
    private TextField player4Field;

    private TextField[] fields;

    @FXML
    public void initialize() {
        fields = new TextField[]{player1Field, player2Field, player3Field, player4Field};
    }

    @FXML
    void validateField(Event event) {
        TextField field = (TextField) event.getSource();
        field.setStyle("-fx-border-color: %s;".formatted(
                field.getText().isBlank() ? "red" : "black"
        ));
    }

    private boolean areFieldsValid() {
        List<TextField> invalidFields = Arrays.stream(fields)
                .filter(f -> f.getText().isBlank())
                .toList();

        invalidFields
                .forEach(f -> f.setStyle("-fx-border-color: red;"));

        if (!invalidFields.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Player Names");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a name for every player before starting the game.");
            alert.showAndWait();
        }
        return invalidFields.isEmpty();
    }

    @FXML
    void startGame() {
        if (!areFieldsValid()) {
            throw new RuntimeException();
        }
    }

    @FXML
    void openSettings() {
        throw new UnsupportedOperationException("Settings are not implemented yet.");
    }
}
