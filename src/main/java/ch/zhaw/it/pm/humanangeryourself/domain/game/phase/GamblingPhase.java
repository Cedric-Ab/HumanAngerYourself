package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

/**
 * Game phase in which the current player gambles. Depending on the gambling settings, the player
 * wins or loses money or receives a card. Not implemented yet.
 */
public class GamblingPhase extends GamePhaseBase {
    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game the game instance this phase belongs to
     */
    public GamblingPhase(@NotNull Game game) {
        super(game);
    }

    /**
     * Carries out the gamble for the current player. Not implemented yet.
     *
     * @return the phase to continue with
     */
    @Override
    public @NotNull GamePhase process() {
        return null;
    }

    /**
     * Handles a decision of the player during gambling. Not implemented yet.
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
