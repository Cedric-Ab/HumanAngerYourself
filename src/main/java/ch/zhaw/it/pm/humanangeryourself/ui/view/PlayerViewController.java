package ch.zhaw.it.pm.humanangeryourself.ui.view;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.Bid;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyProperty;
import ch.zhaw.it.pm.humanangeryourself.api.action.DiscardCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.EndTurn;
import ch.zhaw.it.pm.humanangeryourself.api.action.Gamble;
import ch.zhaw.it.pm.humanangeryourself.api.action.MovePiece;
import ch.zhaw.it.pm.humanangeryourself.api.action.PlayCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.Roll;
import ch.zhaw.it.pm.humanangeryourself.api.action.TargetPiece;
import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PlayerInfo;
import ch.zhaw.it.pm.humanangeryourself.ui.viewmodel.GameViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Comparator;
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
    private VBox additionalActionsBox;
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
        cardsBox.getChildren().clear();
        additionalActionsBox.getChildren().clear();

        if (snapshot == null) {
            gambleButton.setDisable(true);
            rollButton.setDisable(true);
            endTurnButton.setDisable(true);
            return;
        }

        PlayerInfo currentPlayer = snapshot.players().stream()
                .filter(player -> player.color() == snapshot.currentPlayer()
                        || snapshot.currentPlayer() == null && snapshot.decision() != null
                        && player.color() == snapshot.decision().decider())
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

        renderCards(snapshot);
        renderAdditionalActions(snapshot);

        resultLabel.setText(snapshot.message());
    }

    private void renderCards(GameSnapshot snapshot) {
        for (CardType card : snapshot.viewerCards()) {
            Button button = new Button(card.toString());
            Action action = actionForCard(snapshot, card);
            button.setDisable(action == null);
            button.setOnAction(event -> submit(action));
            cardsBox.getChildren().add(button);
        }
    }

    private Action actionForCard(GameSnapshot snapshot, CardType card) {
        if (snapshot.decision() == null) {
            return null;
        }
        return snapshot.decision().availableActions().stream()
                .filter(action -> action instanceof PlayCard play && play.card() == card
                        || action instanceof DiscardCard discard && discard.card() == card)
                .findFirst()
                .orElse(null);
    }

    private void renderAdditionalActions(GameSnapshot snapshot) {
        if (snapshot.decision() == null) {
            return;
        }
        snapshot.decision().availableActions().stream()
                .filter(action -> !(action instanceof Roll || action instanceof Gamble || action instanceof EndTurn
                        || action instanceof MovePiece || action instanceof PlayCard || action instanceof DiscardCard
                        || action instanceof TargetPiece))
                .sorted(Comparator.comparing(this::labelFor))
                .forEach(action -> {
                    Button button = new Button(labelFor(action));
                    button.setMaxWidth(Double.MAX_VALUE);
                    button.setOnAction(event -> submit(action));
                    additionalActionsBox.getChildren().add(button);
                });
    }

    private String labelFor(Action action) {
        return switch (action) {
            case BuyCard buyCard -> "Buy " + buyCard.card();
            case BuyProperty buyProperty -> "Buy " + buyProperty.size() + " property";
            case Bid bid -> "Bid " + bid.amount();
            default -> action.toString();
        };
    }

    private String playerName(GameSnapshot snapshot, ch.zhaw.it.pm.humanangeryourself.api.PlayerColor color) {
        return snapshot.players().stream()
                .filter(player -> player.color() == color)
                .map(PlayerInfo::name)
                .findFirst()
                .orElse(color.toString());
    }

    private void submit(Action action) {
        if (action != null && viewModel != null) {
            viewModel.submit(action);
        }
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
