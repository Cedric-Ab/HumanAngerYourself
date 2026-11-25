package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

/**
 * Interface for cards that have preconditions to be applied in the game.
 */
public interface HasPreconditions extends Card {
    /**
     * Checks if the card can be applied in the current game state for the given player.
     *
     * @param game   the game on which the card is being applied
     * @param player the player attempting to apply the card
     * @return true if the card can be applied, false otherwise
     */
    boolean isApplyable(Game game, Player player);

    /**
     * Asserts that the card can be applied in the current game state for the given player.
     *
     * @param game   the game on which the card is being applied
     * @param player the player attempting to apply the card
     * @throws IllegalStateException if the card cannot be applied
     * @see #isApplyable(Game, Player)
     */
    default void assertApplyable(Game game, Player player) {
        if (!isApplyable(game, player)) {
            throw new IllegalStateException("Card cannot be applied because preconditions are not met.");
        }
    }
}
