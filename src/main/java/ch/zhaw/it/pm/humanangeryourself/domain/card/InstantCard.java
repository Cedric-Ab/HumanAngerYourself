package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

/**
 * A card whose effect is applied immediately when it is drawn, without being
 * added to the player's hand. Compare {@link HandCard}.
 */
public interface InstantCard extends Card {
    /**
     * Applies the effect of this card to the player who drew it.
     *
     * @param game   the game in which the card is drawn
     * @param player the player who drew the card
     */
    @Override
    void apply(Game game, Player player);
}
