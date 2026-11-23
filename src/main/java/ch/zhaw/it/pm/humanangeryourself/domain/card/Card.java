package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;

/**
 * Base abstraction for all cards in the game.
 */
public interface Card {
    /**
     * Returns the type of this card.
     *
     * @return the {@link CardType} of this card
     */
    public CardType type();
}
