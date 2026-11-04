package ch.zhaw.it.pm.humanangeryourself.api.action;

/**
 * Thrown when an action is submitted that is not allowed in the current game state.
 */
public class InvalidActionException extends RuntimeException {

    /**
     * Create a new exception
     *
     * @param message error message
     */
    public InvalidActionException(String message) {
        super(message);
    }
}
