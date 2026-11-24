package ch.zhaw.it.pm.humanangeryourself.ui.view;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.Bid;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyProperty;
import ch.zhaw.it.pm.humanangeryourself.api.action.DiscardCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.EndTurn;
import ch.zhaw.it.pm.humanangeryourself.api.action.Gamble;
import ch.zhaw.it.pm.humanangeryourself.api.action.PlayCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.Roll;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.CardOffer;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.NpcInfo;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PlayerInfo;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult.GambleResult.CardResult;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult.GambleResult.MoneyResult;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult.RollResult;
import ch.zhaw.it.pm.humanangeryourself.ui.viewmodel.GameViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Comparator;
import java.util.Optional;

/**
 * Controller of the player area. Shows whose turn it is, the money and cards of
 * the deciding player,
 * the result of the last roll or gamble, and offers the available actions as
 * buttons.
 * <p>
 * Every button submits an action taken from the available actions of the
 * snapshot,
 * buttons without a matching action are disabled.
 * </p>
 */
public class PlayerViewController {
    private static final String CHOOSE_ACTION_MESSAGE = "Choose an available action.";

    @FXML
    private Label turnLabel;
    @FXML
    private Label moneyLabel;
    @FXML
    private Label resultLabel;
    @FXML
    private HBox cardsBox;
    @FXML
    private VBox offersBox;
    @FXML
    private Button gambleButton;
    @FXML
    private Button rollButton;
    @FXML
    private Button endTurnButton;

    private GameViewModel viewModel;

    /**
     * Creates the controller. Called by the FXML loader.
     */
    public PlayerViewController() {
    }

    /**
     * Connects the player area to the view model and updates it after every change
     * of the game state.
     *
     * @param viewModel the view model of the game to display
     */
    public void setViewModel(GameViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.snapshotProperty().addListener((observable, oldSnapshot, newSnapshot) -> update(newSnapshot));
        viewModel.selectedTileProperty().addListener((observable, oldTile, newTile) ->
                renderOffers(viewModel.snapshotProperty().get()));
        viewModel.errorMessageProperty().addListener((observable, oldMessage, newMessage) -> showError(newMessage));
        update(viewModel.snapshotProperty().get());
    }

    private void update(GameSnapshot snapshot) {
        turnLabel.setText("Turn: " + playerName(snapshot, snapshot.currentPlayer()));
        moneyLabel.setText("Money: " + findPlayer(snapshot, snapshot.viewer()).map(PlayerInfo::money)
                .map(String::valueOf).orElse("--"));
        resultLabel.setText(resultMessage(snapshot));

        bindButton(rollButton, findAction(snapshot, Roll.class));
        bindButton(gambleButton, findAction(snapshot, Gamble.class));
        bindButton(endTurnButton, findAction(snapshot, EndTurn.class));

        renderCards(snapshot);
        renderOffers(snapshot);
    }

    private void showError(String message) {
        if (!message.isBlank()) {
            resultLabel.setText(message);
        }
    }

    private String resultMessage(GameSnapshot snapshot) {
        if (snapshot.winner() != null) {
            return "Winner: " + playerName(snapshot, snapshot.winner());
        }
        if (snapshot.primaryActionResult() != null) {
            return describe(snapshot.primaryActionResult());
        }
        return CHOOSE_ACTION_MESSAGE;
    }

    private String describe(PrimaryActionResult result) {
        return switch (result) {
            case RollResult roll -> "You rolled a " + roll.dieRoll();
            case MoneyResult money when money.amount() > 0 -> "Hurray, you won %,d$".formatted(money.amount());
            case MoneyResult money -> "Oh no, you lost %,d$".formatted(-money.amount());
            case CardResult card -> "You received " + card.cardType();
        };
    }

    /**
     * Shows a button for every card of the viewer. A card can be clicked if it may
     * be played or discarded.
     */
    private void renderCards(GameSnapshot snapshot) {
        cardsBox.getChildren().clear();
        for (CardType card : snapshot.viewerCards()) {
            Button button = new Button(card.toString());
            bindButton(button, findCardAction(snapshot, card));
            cardsBox.getChildren().add(button);
        }
    }

    /**
     * Shows a button for every available bid and every card the NPC on the selected tile sells,
     * followed by the property buttons.
     */
    private void renderOffers(GameSnapshot snapshot) {
        offersBox.getChildren().setAll(snapshot.decision().availableActions().stream()
                .filter(action -> !(action instanceof BuyCard buyCard) || isSelected(buyCard.tile()))
                .flatMap(action -> offerLabel(snapshot, action).map(label -> createOfferButton(label, action)).stream())
                .sorted(Comparator.comparing(Button::getText))
                .toList());
        renderPropertyButtons(snapshot);
    }

    private Button createOfferButton(String label, Action action) {
        Button button = new Button(label);
        button.setMaxWidth(Double.MAX_VALUE);
        bindButton(button, Optional.of(action));
        return button;
    }

    /**
     * Shows a button for every property size if a property can be bought somewhere.
     * The buttons buy on the selected tile. They are disabled if no buyable tile is selected,
     * and for sizes the player cannot afford, because those are not offered by the game.
     */
    private void renderPropertyButtons(GameSnapshot snapshot) {
        if (findAction(snapshot, BuyProperty.class).isEmpty()) {
            return;
        }
        for (PropertySize size : PropertySize.values()) {
            Button button = new Button("Buy " + size + " property");
            button.setMaxWidth(Double.MAX_VALUE);
            bindButton(button, findPropertyAction(snapshot, size));
            offersBox.getChildren().add(button);
        }
    }

    /**
     * Returns the button label of an action shown in the offers area.
     *
     * @return the label, or empty if the action is not shown as an offer
     */
    private static Optional<String> offerLabel(GameSnapshot snapshot, Action action) {
        return switch (action) {
            case BuyCard buyCard -> Optional.of(buyCardLabel(snapshot, buyCard));
            case Bid bid -> Optional.of("Bid " + bid.amount());
            default -> Optional.empty();
        };
    }

    /**
     * Creates the label for buying a card from an NPC, e.g. "Buy SWITCH from Grumpy old Woman (40000)".
     */
    private static String buyCardLabel(GameSnapshot snapshot, BuyCard buyCard) {
        NpcInfo npc = snapshot.npcAt(buyCard.tile()).orElseThrow();
        CardOffer offer = npc.offerFor(buyCard.card()).orElseThrow();

        return "Buy " + buyCard.card() + " from " + npc.name() + " (" + offer.price() + ")";
    }

    private void bindButton(Button button, Optional<? extends Action> action) {
        button.setDisable(action.isEmpty());
        button.setOnAction(event -> action.ifPresent(viewModel::submit));
    }

    private static Optional<Action> findCardAction(GameSnapshot snapshot, CardType card) {
        return snapshot.decision().availableActions().stream()
                .filter(action -> action instanceof PlayCard play && play.card() == card
                        || action instanceof DiscardCard discard && discard.card() == card)
                .findFirst()
                .map(Action.class::cast);
    }

    private Optional<BuyProperty> findPropertyAction(GameSnapshot snapshot, PropertySize size) {
        return snapshot.decision().availableActions().stream()
                .filter(action -> action instanceof BuyProperty buyProperty
                        && buyProperty.size() == size && isSelected(buyProperty.tile()))
                .map(BuyProperty.class::cast)
                .findFirst();
    }

    private boolean isSelected(int tile) {
        Integer selectedTile = viewModel.selectedTileProperty().get();
        return selectedTile != null && selectedTile == tile;
    }

    private static <A extends Action> Optional<A> findAction(GameSnapshot snapshot, Class<A> type) {
        return snapshot.decision().availableActions().stream()
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst();
    }

    private static Optional<PlayerInfo> findPlayer(GameSnapshot snapshot, PlayerColor color) {
        return snapshot.players().stream()
                .filter(player -> player.color() == color)
                .findFirst();
    }

    private static String playerName(GameSnapshot snapshot, PlayerColor color) {
        return findPlayer(snapshot, color).map(PlayerInfo::name).orElse(String.valueOf(color));
    }
}
