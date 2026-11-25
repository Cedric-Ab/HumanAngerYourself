package ch.zhaw.it.pm.humanangeryourself.domain.card.types;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.domain.card.ApplicableCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.Counter;

import java.util.Optional;

/**
 * Hand card that blocks another card.
 *
 * <p><strong>Note:</strong> not yet implemented.</p>
 */
public class BlockCard implements Counter {

    @Override
    public CardType type() {
        return CardType.ANGER_REVERSE;
    }

    @Override
    public Optional<ApplicableCard> apply(ApplicableCard card) {
        return null;
    }
}
