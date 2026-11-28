package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for what all kinds of {@link Card} have in common.
 */
class CardTest {

    /**
     * Every card type has its own card, and that card knows its type.
     */
    @ParameterizedTest
    @EnumSource(CardType.class)
    void cardMatchesItsType(CardType type) {
        assertEquals(type, Card.fromType(type).type());
    }
}
