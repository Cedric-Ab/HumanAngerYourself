package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

/**
 * A card that a player keeps in their hand and plays deliberately at a time of
 * their choosing, as opposed to an {@link InstantCard}, which takes effect immediately.
 */
public interface HandCard extends Card {
    /**
     * Applies the effect of this card to the player who plays it.
     *
     * @param game   the game in which the card is played
     * @param player the player who plays the card
     */
    @Override
    void apply(Game game, Player player);
}
