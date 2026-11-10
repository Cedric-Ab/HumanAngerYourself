package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyProperty;
import ch.zhaw.it.pm.humanangeryourself.api.action.EndTurn;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Game phase at the end of a turn. The current player can buy cards and properties
 * and then end the turn, which starts the {@link StartOfTurnPhase} of the next player.
 */
public class EndOfTurnPhase extends GamePhaseBase {
    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game the game instance this phase belongs to
     */
    public EndOfTurnPhase(@NotNull Game game) {
        super(game);
    }

    /**
     * Offers the current player to end the turn, together with all cards and properties
     * the player can currently buy.
     *
     * @return this phase, as it waits for the player's decision
     */
    @Override
    public @NotNull GamePhase process() {
        List<BuyCard> buyCardList = getGame().getEconomy()
                .getBuyableCards(getCurrentPlayer());

        List<BuyProperty> buyPropertyList = getGame().getEconomy()
                .getBuyableProperties(getCurrentPlayer());

        List<Action> actions = new ArrayList<>();
        actions.add(new EndTurn());
        actions.addAll(buyCardList);
        actions.addAll(buyPropertyList);

        setDecisionInfo(getCurrentPlayer(), actions);
        return this;
    }

    /**
     * Handles the player's choice: ending the turn or buying a card or property.
     *
     * @param player the player who made the decision
     * @param action the chosen action: {@link EndTurn}, {@link BuyCard} or {@link BuyProperty}
     * @return a {@link StartOfTurnPhase} after ending the turn, otherwise this phase again
     * @throws RuntimeException if the action is of an unexpected type
     */
    @Override
    public @NotNull GamePhase handle(Player player, Action action) {
        validatePlayer(player);
        validateAction(action);

        return switch (action) {
            case EndTurn ignored -> endTurn();
            case BuyCard buyCard -> buyCard(player, buyCard);
            case BuyProperty buyProperty -> buyProperty(player, buyProperty);
            default -> throw new RuntimeException("Unexpected action type: " + action.getClass().getName());
        };
    }

    /**
     * Ends the current turn and passes the turn to the next player.
     *
     * @return the {@link StartOfTurnPhase} of the next player
     */
    private GamePhase endTurn() {
        // TODO: paying rent: low priority
        // TODO: auction: lowest priority

        getGame().nextTurn();
        return new StartOfTurnPhase(getGame());
    }

    /**
     * Buys a card for the player. Not implemented yet, the phase is only offered again.
     *
     * @param player  the player who buys the card
     * @param buyCard the chosen purchase
     * @return this phase with an updated decision
     */
    private GamePhase buyCard(Player player, BuyCard buyCard) {
        // TODO: medium priority
        return process();
    }

    /**
     * Buys a property for the player. Not implemented yet, the phase is only offered again.
     *
     * @param player      the player who buys the property
     * @param buyProperty the chosen purchase
     * @return this phase with an updated decision
     */
    private GamePhase buyProperty(Player player, BuyProperty buyProperty) {
        // TODO: low priority
        return process();
    }
}
