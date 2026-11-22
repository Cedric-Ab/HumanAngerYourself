package ch.zhaw.it.pm.humanangeryourself.domain.game;

import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A single turn of the game, belonging to one player.
 */
public class Turn {

    /**
     * The player whose turn it is.
     */
    private final @NotNull Player player;
    /**
     * The result of the last primary action (may be null if no action was performed yet)
     */
    private @Nullable PrimaryActionResult primaryActionResult;

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
     * Returns the result of the last primary action performed by the player during this turn.
     *
     * @return the result of the last primary action, or {@code null} if no action was performed yet
     */
    public @Nullable PrimaryActionResult getPrimaryActionResult() {
        return primaryActionResult;
    }

    /**
     * Sets the result of the last primary action performed by the player during this turn.
     *
     * @param primaryActionResult the result of the last primary action, must not be {@code null}
     */
    public void setPrimaryActionResult(@NotNull PrimaryActionResult primaryActionResult) {
        this.primaryActionResult = primaryActionResult;
    }

    // TODO: add effect stuff
}
