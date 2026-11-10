package ch.zhaw.it.pm.humanangeryourself.domain.game;

import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GamblingOutcome;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A single turn of the game, belonging to one player.
 */
public class Turn {

    /** The player whose turn it is. */
    private final @NotNull Player player;
    private @Nullable GamblingOutcome gamblingOutcome;

    /**
     * Creates a new turn for the given player.
     *
     * @param player the player whose turn it is
     */
    public Turn(@NotNull Player player) {
        this.player = player;
    }

    /**
     * Returns the player whose turn it is.
     *
     * @return the player of this turn
     */
    public @NotNull Player getPlayer() {
        return player;
    }

    public @Nullable GamblingOutcome getGamblingOutcome() {
        return gamblingOutcome;
    }

    public void setGamblingOutcome(@NotNull GamblingOutcome gamblingOutcome) {
        this.gamblingOutcome = gamblingOutcome;
    }

    // TODO: add effect stuff
}
