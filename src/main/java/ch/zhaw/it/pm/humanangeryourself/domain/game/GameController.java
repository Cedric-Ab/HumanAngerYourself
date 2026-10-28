package ch.zhaw.it.pm.humanangeryourself.domain.game;

import ch.zhaw.it.pm.humanangeryourself.api.Action;
import ch.zhaw.it.pm.humanangeryourself.api.DecisionInfo;
import ch.zhaw.it.pm.humanangeryourself.domain.game.phase.GamePhase;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

/**
 * The public interface of the game controller.
 * <p>
 * The game controller is responsible for controlling the game flow and managing the game state.
 * It provides methods for submitting actions, retrieving decisions, and accessing the game instance.
 */
public class GameController {

    private final @NotNull Game game;

    private @NotNull GamePhase currentPhase;

    /**
     * Creates a new instance of the GameController with the specified game instance.
     *
     * @param game the game instance to be controlled by this controller
     */
    public GameController(@NotNull Game game) {
        this.game = game;
    }

    /**
     * Creates a new instance of the GameController with a new Game instance.
     */
    public GameController() {
        this(new Game());
    }

    /**
     * Submits an action for the specified player.
     *
     * @param player the player submitting the action
     * @param action the action being submitted
     */
    public void submit(@NotNull Player player, @NotNull Action action) {
        GamePhase nextPhase = currentPhase.handle(player, action);
        setPhaseTo(nextPhase);
    }

    /**
     * Returns the current decision information for the game.
     *
     * @return the current decision information
     */
    public @NotNull DecisionInfo getDecision() {
        return currentPhase.getDecision();
    }

    /**
     * Returns the game instance that is controlled by this controller.
     *
     * @return the game instance
     */
    public @NotNull Game getGame() {
        return game;
    }

    private void startGame() {
        // TODO: implement
    }

    /**
     * Sets the current phase of the game to the specified phase and processes it.
     * <p>
     * Ensures {@link GamePhase#process()} is called for each phase.
     *
     * @param phase the new phase to set as the current phase, may be the same as the current phase
     */
    private void setPhaseTo(@NotNull GamePhase phase) {
        if (phase == currentPhase) {
            return;
        }

        currentPhase = phase;

        GamePhase nextPhase = currentPhase.process();
        setPhaseTo(nextPhase);
    }
}
