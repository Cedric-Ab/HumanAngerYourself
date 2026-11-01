package ch.zhaw.it.pm.humanangeryourself.api;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;

/**
 * The game service is the interface between the UI and the domain logic.
 * <p>
 * This interface is required, because the UI and the domain logic may or may not
 * live on different machines. The UI only depends on this interface, so local and
 * remote implementations are interchangeable.
 */
public interface GameService {
    /**
     * Informs the game logic about an action of the player who currently has to decide.
     *
     * @param action performed action
     * @throws InvalidActionException if the action is not allowed in the current game state
     *
     */
    void submit(Action action);

    /**
     * Register a listener, which will be informed about changes in game state.
     *
     * @param listener object to be informed about changes
     */
    void addListener(GameListener listener);

    /**
     * Return a snapshot representing the current game state.
     *
     * @return latest snapshot
     */
    GameSnapshot getSnapshot();
}
