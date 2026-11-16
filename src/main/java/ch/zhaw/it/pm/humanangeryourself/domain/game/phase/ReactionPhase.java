package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

/**
 * Game phase in which players can react to a played card, for example by countering it
 * (see {@link ch.zhaw.it.pm.humanangeryourself.api.CardType#isCounterable()}).
 * After the reactions, the game continues with the next phase.
 */
public class ReactionPhase extends NonFlowGamePhase {
    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game      the game instance this phase belongs to
     * @param nextPhase the next game phase to transition to after this phase is completed
     */
    public ReactionPhase(@NotNull Game game, @NotNull GamePhase nextPhase) {
        super(game, nextPhase);
    }

    /**
     * Offers the players the possible reactions. Not implemented yet.
     *
     * @return the phase to continue with
     */
    @Override
    public @NotNull GamePhase process() {
        return null;
    }

    /**
     * Handles a player's reaction. Not implemented yet.
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
