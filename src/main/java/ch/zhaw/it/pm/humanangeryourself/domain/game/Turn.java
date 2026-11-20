package ch.zhaw.it.pm.humanangeryourself.domain.game;

import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

/**
 * A single turn of the game, belonging to one player.
 */
public class Turn {

    /**
     * The player whose turn it is.
     */
    private final Player player;
    /**
     * The result of the last primary action (may be null if no action was performed yet)
     */
    private PrimaryActionResult primaryActionResult;

    /**
     * Creates a new turn for the given player.
     *
     * @param player the player whose turn it is
     */
    public Turn(Player player) {
        this.player = player;
    }

    /**
     * Returns the player whose turn it is.
     *
     * @return the player of this turn
     */
    public Player getPlayer() {
        return player;
    }

    public PrimaryActionResult getPrimaryActionResult() {
        return primaryActionResult;
    }

    public void setPrimaryActionResult(PrimaryActionResult primaryActionResult) {
        this.primaryActionResult = primaryActionResult;
    }

    // TODO: add effect stuff
}
