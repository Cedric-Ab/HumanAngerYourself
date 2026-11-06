package ch.zhaw.it.pm.humanangeryourself.api;

import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;

/**
 * Notified by a {@link GameService} whenever the game state has changed.
 * <p>
 * Receives the new state as an immutable {@link GameSnapshot},
 * so the UI never accesses the domain directly.
 */
@FunctionalInterface
public interface GameListener {

    /**
     * Called after every change of the game state.
     *
     * @param snapshot the new game state from the view of the current decider
     */
    void onGameChanged(GameSnapshot snapshot);
}