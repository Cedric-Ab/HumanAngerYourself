package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;
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
        List<Action> actions = new ArrayList<>();
        actions.add(new EndTurn());
        actions.addAll(List.of(new BuyCard(CardType.CHARITY), new BuyCard(CardType.ANGER_REVERSE)));
        actions.addAll(List.of(new BuyProperty(3, PropertySize.MEDIUM)));

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
        // TODO: paying rent
        // TODO: auction

        getGame().nextTurn();
        return new StartOfTurnPhase(getGame());
    }

    private GamePhase buyCard(Player player, BuyCard buyCard) {
        // TODO: implement
        return process();
    }

    private GamePhase buyProperty(Player player, BuyProperty buyProperty) {
        // TODO: implement
        return process();
    }
}
