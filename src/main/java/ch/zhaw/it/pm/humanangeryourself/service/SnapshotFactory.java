package ch.zhaw.it.pm.humanangeryourself.service;

import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;

/**
 * Creates {@link GameSnapshot}s from the current game state.
 * <p>
 * A snapshot is a read only copy of the game for one viewer. It shall only contain API types
 * so the UI (and later the network) never gets access to domain objects.
 * </p>
 */
public final class SnapshotFactory {

    /**
     * // util class, no instances.
     */
    private SnapshotFactory() {
        // util class, no instances
    }

    /**
     * Creates a snapshot of the game as seen by the given viewer.
     *
     * @return a new immutable snapshot
     */
    public static GameSnapshot create() {
        return null;
    }
}
