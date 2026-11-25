package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult.RollResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link SpeedBoostCard}.
 */
class SpeedBoostCardTest extends CardTestBase {

    private final SpeedBoostCard card = new SpeedBoostCard();

    /**
     * The rolled number is doubled.
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    void doublesRoll(int roll) {
        when(movementRules.rollDie()).thenReturn(roll);

        play(card, red);

        assertEquals(new RollResult(2 * roll), game.getTurn().getPrimaryActionResult());
    }
}
