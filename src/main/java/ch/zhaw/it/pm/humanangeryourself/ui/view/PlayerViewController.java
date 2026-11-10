package ch.zhaw.it.pm.humanangeryourself.ui.view;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.EndTurn;
import ch.zhaw.it.pm.humanangeryourself.api.action.Gamble;
import ch.zhaw.it.pm.humanangeryourself.api.action.Roll;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PlayerInfo;
import ch.zhaw.it.pm.humanangeryourself.ui.viewmodel.GameViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.Optional;

/**
 * Controller for the player information and action area.
 */
public class PlayerViewController {

    @FXML
    private Label playerNameLabel;
    @FXML
    private Label moneyLabel;
    @FXML
    private Label resultLabel;
    @FXML
    private HBox cardsBox;
    @FXML
    private Button gambleButton;
    @FXML
    private Button rollButton;
    @FXML
    private Button endTurnButton;

    private GameViewModel viewModel;

    public void setViewModel(GameViewModel viewModel) {
        this.viewModel = viewModel;

        viewModel.snapshotProperty().addListener(
                (ignored, oldSnapshot, newSnapshot) -> update(newSnapshot));

        viewModel.messageProperty().addListener(
                (ignored, oldMessage, newMessage) -> {
                    if (newMessage != null && !newMessage.isBlank()) {
                        resultLabel.setText(newMessage);
                    }
                });

        update(viewModel.snapshotProperty().get());
    }

    @FXML
    private void roll() {
        submitAvailable(Roll.class, "Rolling die...");
    }

    @FXML
    private void gamble() {
        submitAvailable(Gamble.class, "Gambling...");
    }

    @FXML
    private void endTurn() {
        submitAvailable(EndTurn.class, "Ending turn...");
    }

    /**
     * Submits an available action to the backend through the ViewModel.
     */
    private void submitAvailable(Class<? extends Action> type, String pendingMessage) {
        if (viewModel == null) {
            return;
        }

        findAction(viewModel.snapshotProperty().get(), type).ifPresent(action -> {
            resultLabel.setText(pendingMessage);

            gambleButton.setDisable(true);
            rollButton.setDisable(true);
            endTurnButton.setDisable(true);

            viewModel.submit(action);
        });
    }

    /**
     * Updates the displayed player information and available actions.
     */
    private void update(GameSnapshot snapshot) {
        if (snapshot == null) {
            gambleButton.setDisable(true);
            rollButton.setDisable(true);
            endTurnButton.setDisable(true);
            return;
        }

        PlayerInfo currentPlayer = snapshot.players().stream()
                .filter(player -> player.color() == snapshot.currentPlayer())
                .findFirst()
                .orElse(null);

        String name = currentPlayer == null
                ? snapshot.decision() == null
                        ? "Waiting"
                        : String.valueOf(snapshot.decision().decider())
                : currentPlayer.name();

        playerNameLabel.setText("Turn: " + name);

        PlayerInfo viewer = snapshot.players().stream()
                .filter(player -> player.color() == snapshot.viewer())
                .findFirst()
                .orElse(null);

        moneyLabel.setText(
                viewer == null ? "Money: --" : "Money: " + viewer.money());

        gambleButton.setDisable(findAction(snapshot, Gamble.class).isEmpty());
        rollButton.setDisable(findAction(snapshot, Roll.class).isEmpty());
        endTurnButton.setDisable(findAction(snapshot, EndTurn.class).isEmpty());

        // TODO: Display player cards once the card mechanic is implemented.
        // TODO: Display dice and gambling results from backend-provided data.
    }

    /**
     * Finds an available action of the requested type.
     */
    private Optional<Action> findAction(GameSnapshot snapshot, Class<? extends Action> type) {
        if (snapshot == null || snapshot.decision() == null) {
            return Optional.empty();
        }

        return snapshot.decision().availableActions().stream()
                .filter(type::isInstance)
                .findFirst()
                .map(Action.class::cast);
    }
}