package ch.zhaw.it.pm.humanangeryourself.ui.view;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerSetup;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Controller of the start screen, where up to four players enter their names
 * before the game starts.
 * Each name field belongs to one color, empty fields are left out of the game.
 */
public class StartScreenViewController {
    /** Minimum number of names needed to start a game. */
    private static final int MIN_PLAYERS = 2;
    /** Style class of a name field that has to be filled in. */
    private static final String INVALID_STYLE = "invalid";

    @FXML
    private TextField redPlayerField;
    @FXML
    private TextField yellowPlayerField;
    @FXML
    private TextField bluePlayerField;
    @FXML
    private TextField greenPlayerField;

    /** Name field of every color, in the order the fields are shown. */
    private final Map<PlayerColor, TextField> nameFields = new LinkedHashMap<>();

    /** Called with the entered players when the game is started. */
    private Consumer<List<PlayerSetup>> onGameStart;

    /**
     * Creates the controller. Called by the FXML loader.
     */
    public StartScreenViewController() {
    }

    /**
     * Called by the FXML loader. Assigns every name field its color.
     */
    @FXML
    public void initialize() {
        nameFields.put(PlayerColor.RED, redPlayerField);
        nameFields.put(PlayerColor.YELLOW, yellowPlayerField);
        nameFields.put(PlayerColor.BLUE, bluePlayerField);
        nameFields.put(PlayerColor.GREEN, greenPlayerField);
    }

    /**
     * Sets the action that starts the game with the entered players.
     *
     * @param onGameStart called with the entered players when the game is started
     * @throws NullPointerException if {@code onGameStart} is {@code null}
     */
    public void setOnGameStart(Consumer<List<PlayerSetup>> onGameStart) {
        this.onGameStart = Objects.requireNonNull(onGameStart, "onGameStart must not be null");
    }

    /**
     * Starts the game with every player who entered a name. If fewer than
     * {@value #MIN_PLAYERS}
     * names are entered, the empty fields are marked and a warning is shown
     * instead.
     */
    @FXML
    private void startGame() {
        List<PlayerSetup> playerSetups = readPlayerSetups();
        if (playerSetups.size() < MIN_PLAYERS) {
            markEmptyFields();
            showNotEnoughPlayersWarning();
            return;
        }
        onGameStart.accept(playerSetups);
    }

    /**
     * Removes the invalid mark of the edited field as soon as it contains a name.
     *
     * @param event the event fired by a name field, whose source must be a
     *              {@link TextField}
     */
    @FXML
    private void validateField(Event event) {
        TextField field = (TextField) event.getSource();
        if (!field.getText().isBlank()) {
            field.getStyleClass().remove(INVALID_STYLE);
        }
    }

    /**
     * Reads the entered names and assigns each one the color of its field. Empty
     * fields are skipped.
     *
     * @return name and color of every entered player, in field order
     */
    private List<PlayerSetup> readPlayerSetups() {
        return nameFields.entrySet().stream()
                .filter(entry -> !entry.getValue().getText().isBlank())
                .map(entry -> new PlayerSetup(entry.getValue().getText().strip(), entry.getKey()))
                .toList();
    }

    private void markEmptyFields() {
        nameFields.values().stream()
                .filter(field -> field.getText().isBlank())
                .filter(field -> !field.getStyleClass().contains(INVALID_STYLE))
                .forEach(field -> field.getStyleClass().add(INVALID_STYLE));
    }

    private void showNotEnoughPlayersWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Not Enough Players");
        alert.setHeaderText(null);
        alert.setContentText("Please enter the names of at least %d players.".formatted(MIN_PLAYERS));
        alert.showAndWait();
    }
}
