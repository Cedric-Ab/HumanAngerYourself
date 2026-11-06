package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyProperty;
import ch.zhaw.it.pm.humanangeryourself.api.action.EndTurn;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EndOfTurnPhase extends GamePhaseBase {
    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game the game instance this phase belongs to
     */
    public EndOfTurnPhase(@NotNull Game game) {
        super(game);
    }

    @Override
    public @NotNull GamePhase process() {
        List<CardType> buyableCards = getGame().getEconomy()
                .getBuyableCards(getCurrentPlayer());
        List<BuyCard> buyCardList = buyableCards.stream()
                .map(BuyCard::new)
                .toList();

        List<BuyProperty> buyPropertyList = getGame().getEconomy()
                .getBuyableProperties(getCurrentPlayer());

        List<Action> actions = new ArrayList<>();
        actions.add(new EndTurn());
        actions.addAll(buyCardList);
        actions.addAll(buyPropertyList);

        setDecisionInfo(getCurrentPlayer(), actions);
        return this;
    }

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

    private GamePhase endTurn() {
        // TODO: paying rent: low priority
        // TODO: auction: lowest priority

        getGame().nextTurn();
        return new StartOfTurnPhase(getGame());
    }

    private GamePhase buyCard(Player player, BuyCard buyCard) {
        // TODO: medium priority
        return process();
    }

    private GamePhase buyProperty(Player player, BuyProperty buyProperty) {
        // TODO: low priority
        return process();
    }
}
