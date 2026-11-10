package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

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
     * Checks whether the current player has to discard cards and publishes the decision if so.
     * Not implemented yet.
     *
     * @return the phase to continue with
     */
    @Override
    public @NotNull GamePhase process() {
        return null;
    }

    /**
     * Handles the player's choice of which card to discard. Not implemented yet.
     *
     * @param player the player who made the decision
     * @param action the chosen action
     * @return the phase to continue with
     */
    @Override
    public @NotNull GamePhase handle(Player player, Action action) {
        return null;
    }
}
