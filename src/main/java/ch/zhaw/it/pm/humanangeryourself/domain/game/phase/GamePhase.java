package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.DecisionInfo;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a phase in the game, which can be processed, provide decision information, and handle player actions.
 * <p>
 * Subclasses of this interface should implement the specific behavior for each game phase,
 * including processing the phase, providing decision information, and handling player actions.
 */
public interface GamePhase {
    /**
     * Processes the current game phase up to potentially required user input.
     *
     * @return this game phase if a decision is required, or the next game phase if no decision is needed
     */
    @NotNull GamePhase process();

    /**
     * Returns the decision information the player must make in this game phase.
     *
     * @return the decision information for the current game phase
     */
    @NotNull DecisionInfo getDecision();

    /**
     * Handles the player's action and returns the next game phase.
     *
     * @param player the player making the action
     * @param action the action taken by the player
     * @return this game phase if the user must make another decision,
     *         or the next game phase if no further decisions are required
     */
    @NotNull GamePhase handle(Player player, Action action);
}
