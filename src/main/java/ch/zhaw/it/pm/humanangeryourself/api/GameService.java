package ch.zhaw.it.pm.humanangeryourself.api;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;

/**
 * The game service is the interface between the UI and the domain logic.
 * This interface is required, because the UI and domain logic may
 * or may not live on different machines.
 */
public interface GameService extends GameListener {
    /**
     * Inform the game logic about an action the current viewer performed.
     * @param action performed action
     */
    void submit(Action action);

    /**
     * Register a listener, which will be informed about changes in game state.
     * @param listener object to be informed about changes
     */
    void addListener(GameListener listener);

    /**
     * Return a snapshot representing the game state.
     * @return latest snapshot
     */
    GameSnapshot getSnapshot();
}
