package ch.zhaw.it.pm.humanangeryourself.ui.view;

import javafx.event.ActionEvent;
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

    private void onFirstStartGameAttempt() {
        fields = new TextField[]{player1Field, player2Field, player3Field, player4Field};

        Arrays.stream(fields)
                .forEach(f ->
                        f.onActionProperty().addListener(e ->
                                f.setStyle("-fx-border-color: %s;".formatted(
                                        f.getText().isBlank() ? "red" : "none"
                                ))
                        )
                );

        Arrays.stream(fields)
                .filter(f -> f.getText().isBlank())
                .forEach(f -> f.setStyle("-fx-border-color: red;"));
    }

    @FXML
    void startGame(ActionEvent event) {
        if (fields == null) onFirstStartGameAttempt();

        if (!areFieldsValid()) return;
    }

    @FXML
    void openSettings(ActionEvent event) {
        throw new UnsupportedOperationException("Settings are not implemented yet.");
    }
}
