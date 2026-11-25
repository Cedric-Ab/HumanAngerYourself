package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyProperty;
import ch.zhaw.it.pm.humanangeryourself.api.action.EndTurn;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;

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
        List<BuyCard> buyCardList = getBuyableNpcCards();

        List<BuyProperty> buyPropertyList = getGame().getEconomy()
                .getBuyableProperties(getCurrentPlayer(), getGame().getBoard());

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
     * Buys a card from an NPC for the player, the player pays the price and gets the card.
     * Afterwards no further NPC card can be bought in this turn. If the player now holds more cards
     * than allowed, a card has to be discarded first, then the player is back in this phase.
     *
     * @param player  the player who buys the card
     * @param buyCard the chosen purchase
     * @return a {@link DiscardPhase} that leads back to this phase
     */
    private GamePhase buyCard(Player player, BuyCard buyCard) {
        Tile npcTile = getGame().getBoard().getTileById(buyCard.tile());
        getGame().getEconomy().buyCard(player, npcTile, buyCard.card());
        getGame().getTurn().markNpcCardBought();

        return new DiscardPhase(getGame(), this);
    }

    /**
     * Buys a property for the player.
     *
     * @param player      the player who buys the property
     * @param buyProperty the chosen purchase
     * @return this phase with an updated decision
     */
    private GamePhase buyProperty(Player player, BuyProperty buyProperty) {
        getGame().getEconomy().buyProperty(player, getGame().getBoard().getTileById(buyProperty.tile()), buyProperty.size());
        // TODO: enforce that the player cannot buy another property (maybe???)
        return process();
    }

    /**
     * Returns the cards the current player can buy from NPCs.
     * Only one NPC card can be bought per turn.
     *
     * @return the possible {@link BuyCard} actions, empty if a card was already bought this turn
     */
    private List<BuyCard> getBuyableNpcCards() {
        if (getGame().getTurn().hasBoughtNpcCard()) {
            return List.of();
        }
        return getGame().getEconomy().getBuyableCards(getCurrentPlayer());
    }
}
