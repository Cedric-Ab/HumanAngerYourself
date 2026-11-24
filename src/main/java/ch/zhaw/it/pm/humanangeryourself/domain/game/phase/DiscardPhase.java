package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.DiscardCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.InvalidActionException;
import ch.zhaw.it.pm.humanangeryourself.domain.card.Card;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Game phase in which a player who holds more cards than allowed has to discard cards.
 * Once the hand limit is met, the game continues with the next phase.
 */
public class DiscardPhase extends NonFlowGamePhase {
    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game      the game instance this phase belongs to
     * @param nextPhase the next game phase to transition to after this phase is completed
     */
    public DiscardPhase(@NotNull Game game, @NotNull GamePhase nextPhase) {
        super(game, nextPhase);
    }


    /**
     * Checks whether the current player has to discard cards and offers one {@link DiscardCard}
     * per card type in the player's hand if so.
     *
     * @return this phase if a card has to be discarded, otherwise the next phase
     */
    @Override
    public @NotNull GamePhase process() {
        Player player = getCurrentPlayer();
        if (player.getCards().size() <= getGame().getConfig().maxHandSize()) {
            return getNextPhase();
        }

        List<DiscardCard> options = player.getCards().stream()
                .map(Card::type)
                .distinct()
                .map(DiscardCard::new)
                .toList();
        setDecisionInfo(player, options);
        return this;
    }


    /**
     * Removes the chosen card from the player's hand.
     *
     * @param player the player who made the decision
     * @param action the chosen action, must be a {@link DiscardCard}
     * @return a new discard phase, which checks the hand limit again
     * @throws InvalidActionException if the player or the action is not allowed in this phase
     */
    @Override
    public @NotNull GamePhase handle(Player player, Action action) {
        validatePlayer(player);
        validateAction(action);

        if (!(action instanceof DiscardCard(CardType cardType))) {
            throw new InvalidActionException(action, "Action is not allowed");
        }
        player.removeCard(player.getCard(cardType));

        return new DiscardPhase(getGame(), getNextPhase());
    }
}
