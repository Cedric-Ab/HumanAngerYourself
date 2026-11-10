package ch.zhaw.it.pm.humanangeryourself.api.action;

/**
 * Thrown when an action is submitted that is not allowed in the current game state.
 */
public class InvalidActionException extends RuntimeException {

    /** The action that was rejected. */
    private final Action action;

    /**
     * Create a new exception
     *
     * @param action  the invalid action
     * @param message error message
     */
    public InvalidActionException(Action action, String message) {
        super(message);
        this.action = action;
    }

    /**
     * Returns the action that caused the exception.
     *
     * @return the invalid action
     */
    public Action getAction() {
        return action;
    }
}
