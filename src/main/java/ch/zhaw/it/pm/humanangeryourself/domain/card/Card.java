package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

/**
 * Base abstraction for all cards in the game.
 */
public interface Card {

    boolean preconditionsMet();

    /**
     * Returns the type of this card.
     *
     * @return the {@link CardType} of this card
     */
    CardType type();

    /**
     * Applies the effect of this card.
     *
     * @param game   the game on which the card is applied
     * @param player the player on which the card is applied
     */
    void apply(Game game, Player player);
}
