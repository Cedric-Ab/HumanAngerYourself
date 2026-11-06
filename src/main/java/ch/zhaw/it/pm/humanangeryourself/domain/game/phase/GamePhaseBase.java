package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.DecisionInfo;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * An abstract base class for game phases, providing common functionality for processing phases.
 * <p>
 * Common functionality includes managing the game instance,
 * handling decision information, and validating player actions.
 */
public abstract class GamePhaseBase implements GamePhase {

    private final @NotNull Game game;

    private @Nullable DecisionInfo decisionInfo;

    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game the game instance this phase belongs to
     */
    public GamePhaseBase(@NotNull Game game) {
        this.game = game;
    }

    /**
     * Returns the game instance associated with this game phase.
     *
     * @return the game instance
     */
    protected @NotNull Game getGame() {
        return game;
    }

    /**
     * Returns the current player whose turn it is in the game.
     *
     * @return the current player
     */
    protected @NotNull Player getCurrentPlayer() {
        return getGame().getTurn().getPlayer();
    }

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
     * Use this to create the {@code DecisionInfo} object in {@link GamePhase#process()} and set it for this game phase.
     *
     * @param decisionInfo the decision information to be set for this game phase
     */
    protected void setDecisionInfo(@NotNull DecisionInfo decisionInfo) {
        this.decisionInfo = decisionInfo;
    }

    /**
     * Sets the decision information for the current game phase using the provided player and available actions.
     *
     * @param player           the player for which the decision information is relevant
     * @param availableActions the list of actions available to the player in this game phase
     * @see DecisionInfo
     */
    protected void setDecisionInfo(@NotNull Player player,
                                   @NotNull List<? extends @NotNull Action> availableActions) {
        setDecisionInfo(new DecisionInfo(player.getColor(), availableActions));
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
     * Validates that the provided action is a possible action in the context of this game phase.
     *
     * @param action the action to be validated
     * @throws IllegalArgumentException if the provided action is illegal in this game phase
     */
    protected void validateAction(@NotNull Action action) {
        if (isIllegalAction(action)) {
            throw new IllegalArgumentException("Action is not available in this phase.");
        }
    }

    /**
     * Validates that the provided player is the one associated with the current decision in this game phase.
     *
     * @param player the player to be validated
     * @throws IllegalArgumentException if the provided player is not the one associated with the current decision
     */
    protected void validatePlayer(@NotNull Player player) {
        if (!getDecision().decider().equals(player.getColor())) {
            throw new IllegalArgumentException(
                    "Only the player associated with this decision can perform actions in this phase.");
        }
    }
}
