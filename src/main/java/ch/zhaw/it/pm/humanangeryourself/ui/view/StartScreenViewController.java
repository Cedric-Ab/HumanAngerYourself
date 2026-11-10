package ch.zhaw.it.pm.humanangeryourself.ui.view;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.List;

/**
 * Controller of the start screen, where the four players enter their names before the game starts.
 * Empty name fields are highlighted with a red border.
 */
public class StartScreenViewController {

    /** Name field of the first player, injected by the FXML loader. */
    @FXML
    private TextField player1Field;

    /** Name field of the second player, injected by the FXML loader. */
    @FXML
    private TextField player2Field;

    /** Name field of the third player, injected by the FXML loader. */
    @FXML
    private TextField player3Field;

    /** Name field of the fourth player, injected by the FXML loader. */
    @FXML
    private TextField player4Field;

    /** All four name fields in player order, for validating them together. */
    private TextField[] fields;

    /**
     * Called by the FXML loader after the FXML file has been loaded. Collects the name fields
     * into an array.
     */
    @FXML
    public void initialize() {
        fields = new TextField[]{player1Field, player2Field, player3Field, player4Field};
    }

    /**
     * Validates the text field that triggered the event. The border turns red if the field is
     * blank and black otherwise.
     *
     * @param event the event fired by a name field, whose source must be a {@link TextField}
     */
    @FXML
    void validateField(Event event) {
        TextField field = (TextField) event.getSource();
        field.setStyle("-fx-border-color: %s;".formatted(
                field.getText().isBlank() ? "red" : "black"
        ));
    }

    /**
     * Checks that every player has entered a name. Blank fields get a red border, and a warning
     * dialog is shown if at least one is blank.
     *
     * @return {@code true} if all name fields are filled in
     */
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

    /**
     * Starts the game after validating the player names. Starting the game is not implemented yet.
     *
     * @throws RuntimeException if at least one player name is missing
     */
    @FXML
    void startGame() {
        if (!areFieldsValid()) {
            throw new RuntimeException();
        }
    }

    /**
     * Opens the settings screen. Not implemented yet.
     *
     * @throws UnsupportedOperationException always, until the settings are implemented
     */
    @FXML
    void openSettings() {
        throw new UnsupportedOperationException("Settings are not implemented yet.");
    }
}
