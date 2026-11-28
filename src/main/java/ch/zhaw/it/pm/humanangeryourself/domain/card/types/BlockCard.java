package ch.zhaw.it.pm.humanangeryourself.domain.card.types;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.domain.card.ApplicableCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.Counter;

import java.util.Objects;
import java.util.Optional;

/**
 * Hand card that blocks another card.
 */
public class BlockCard implements Counter {

    /**
     * Returns the type of this card.
     *
     * @return {@link CardType#BLOCK}
     */
    @Override
    public CardType type() {
        return CardType.BLOCK;
    }

    /**
     * Blocks the given card completely.
     *
     * @param card the card to block, must not be {@code null}
     * @return always {@link Optional#empty()}, as nothing of the blocked card remains
     * @throws NullPointerException if {@code card} is {@code null}
     */
    @Override
    public Optional<ApplicableCard> apply(ApplicableCard card) {
        Objects.requireNonNull(card, "card must not be null");
        return Optional.empty();
    }
}
