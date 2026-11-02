package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.DecisionInfo;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a phase in the game, which can be processed, provide decision information, and handle player actions.
 * <p>
 * Subclasses of this abstract class should implement the specific behavior for each game phase.
 */
public abstract class GamePhase {

    private final @NotNull Game game;

    private @Nullable DecisionInfo decisionInfo;

    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game the game instance this phase belongs to
     */
    public GamePhase(@NotNull Game game) {
        this.game = game;
    }

    /**
     * Processes the current game phase up to potentially required user input.
     *
     * @return this game phase if a decision is required, or the next game phase if no decision is needed
     */
    public abstract @NotNull GamePhase process();

    /**
     * Returns the decision information the player must make in this game phase.
     *
     * @return the decision information for the current game phase
     * @throws IllegalStateException if the decision information has not been set for this game phase
     */
    public @NotNull DecisionInfo getDecision() {
        if (decisionInfo == null) {
            throw new IllegalStateException("Decision information has not been set for this game phase." +
                    "Ensure that process() has been called before calling getDecision().");
        }
        return decisionInfo;
    }

    /**
     * Sets the decision information for the current game phase.
     * <p>
     * Use this to create the {@code DecisionInfo} object in {@link #process()} and set it for this game phase.
     *
     * @param decisionInfo the decision information to be set for this game phase
     */
    protected void setDecisionInfo(@NotNull DecisionInfo decisionInfo) {
        this.decisionInfo = decisionInfo;
    }

    /**
     * Checks if the provided action is illegal in the context of this game phase.
     *
     * @param action the action to be checked for legality
     * @return true if the action is illegal, false otherwise
     */
    protected boolean isIllegalAction(@NotNull Action action) {
        return !getDecision().availableActions().contains(action);
    }

    /**
     * Handles the player's action and returns the next game phase.
     *
     * @param player the player making the action
     * @param action the action taken by the player
     * @return this game phase if the action is invalid or the user must make another decision,
     *         or the next game phase if no further decisions are required
     */
    public abstract @NotNull GamePhase handle(Player player, Action action);

    /**
     * Returns the game instance associated with this game phase.
     *
     * @return the game instance
     */
    protected @NotNull Game getGame() {
        return game;
    }
}
