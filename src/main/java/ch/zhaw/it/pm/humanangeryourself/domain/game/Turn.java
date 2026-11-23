package ch.zhaw.it.pm.humanangeryourself.domain.game;

import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GamblingOutcome;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.RollOutcome;
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
    private @Nullable RollOutcome rollOutcome;

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

    /**
     * Returns the outcome of the gambling spin of this turn.
     *
     * @return the gambling outcome, or {@code null} if no gambling outcome has been set
     */
    public @Nullable GamblingOutcome getGamblingOutcome() {
        return gamblingOutcome;
    }

    /**
     * Sets the outcome of the gambling spin of this turn.
     *
     * @param gamblingOutcome the gambling outcome to record
     */
    public void setGamblingOutcome(@NotNull GamblingOutcome gamblingOutcome) {
        this.gamblingOutcome = gamblingOutcome;
    }

    /**
     * Returns the outcome of the die roll of this turn.
     *
     * @return the roll outcome, or {@code null} if no roll outcome has been set
     */
    public @Nullable RollOutcome getRollOutcome() {
        return rollOutcome;
    }

    /**
     * Sets the outcome of the die roll of this turn.
     *
     * @param rollOutcome the roll outcome to record
     */
    public void setRollOutcome(@NotNull RollOutcome rollOutcome) {
        this.rollOutcome = rollOutcome;
    }

    // TODO: add effect stuff
}
