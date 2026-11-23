package ch.zhaw.it.pm.humanangeryourself.api.action;

import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

/**
 * Thrown when an action is submitted that is not allowed in the current game state.
 */
public class InvalidActionException extends RuntimeException {

    private final Player player;
    private final Action action;

    /**
     * Create a new exception
     *
     * @param player  the player who submitted the invalid action
     * @param action  the invalid action
     * @param message error message
     */
    public InvalidActionException(Player player, Action action, String message) {
        super(message);
        this.player = player;
        this.action = action;
    }

    /**
     * Create a new exception
     *
     * @param action  the invalid action
     * @param message error message
     */
    public InvalidActionException(Action action, String message) {
        this(null, action, message);
    }

    /**
     * Create a new exception
     *
     * @param player  the player who submitted the invalid action
     * @param message error message
     */
    public InvalidActionException(Player player, String message) {
        this(player, null, message);
    }

    /**
     * Create a new exception
     *
     * @param message error message
     */
    public InvalidActionException(String message) {
        this(null, null, message);
    }

    /**
     * Returns the player who submitted the invalid action.
     *
     * @return the player who submitted the invalid action, or null if not applicable
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the action that caused the exception.
     *
     * @return the invalid action, or null if not applicable
     */
    public Action getAction() {
        return action;
    }
}
