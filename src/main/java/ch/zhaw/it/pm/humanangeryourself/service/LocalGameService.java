package ch.zhaw.it.pm.humanangeryourself.service;

import ch.zhaw.it.pm.humanangeryourself.api.Action;
import ch.zhaw.it.pm.humanangeryourself.api.GameListener;
import ch.zhaw.it.pm.humanangeryourself.api.GameSnapshot;


/**
 * Local (hotseat) implementation of {@link GameService}.
 * TODO add description here
 */
public class LocalGameService {

    /**
     * Creates a service for the given game and registers itself as observer.
     */
    public LocalGameService() {
        // TODO add game object and call the addObserver with this
    }

    /**
     * Returns the current game state <b> as seen by the current viewer </b>.
     * @return a new snapshot of the game
     */
    public GameSnapshot getSnapshot() {
        // TOOD
        return SnapshotFactory.create();
    }

    /**
     * Submits an action on behalf of the current viewer.
     *
     * @param action the action chosen in the UI
     */
    public void submit(Action action) {
        // TODO
    }

    /**
     * Registers a listener that is informed about every game change.
     * @param listener the listener to add
     */
    public void addListener(GameListener listener) {
        // TODO
    }

    /**
     * Called by the game after every change.
     * Creates one snapshot and sends it to all listeners.
     */
    public void onGameChanged() {
        // TODO
    }
}
