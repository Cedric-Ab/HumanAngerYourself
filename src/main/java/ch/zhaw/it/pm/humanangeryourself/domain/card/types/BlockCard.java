package ch.zhaw.it.pm.humanangeryourself.domain.card.types;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.domain.card.ApplicableCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.Counter;

import java.util.Optional;

/**
 * Hand card that blocks another card.
 */
public class BlockCard implements Counter {

    @Override
    public CardType type() {
        return CardType.BLOCK;
    }

    @Override
    public Optional<ApplicableCard> apply(ApplicableCard card) {
        return Optional.empty();
    }
}
