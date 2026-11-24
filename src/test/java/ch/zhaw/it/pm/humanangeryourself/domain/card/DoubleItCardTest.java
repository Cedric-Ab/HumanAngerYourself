package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.action.InvalidActionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link DoubleItCard}.
 */
class DoubleItCardTest extends CardTestBase {

    private final DoubleItCard card = new DoubleItCard();

    @BeforeEach
    void setUp() {
        setMoney(red, 1_000);
        setMoney(yellow, 1_000);
        setMoney(blue, 1_000);
    }

    /**
     * The next player gets twice the debt, the player is debt free and nobody else is affected.
     */
    @Test
    void passesDoubledDebtToNextPlayer() {
        setMoney(red, -200);

        play(card, red);

        assertEquals(0, red.getMoney());
        assertEquals(600, yellow.getMoney());
        assertEquals(1_000, blue.getMoney());
    }

    /**
     * After the last player comes the first one again.
     */
    @Test
    void lastPlayerPassesToFirst() {
        setMoney(blue, -200);

        play(card, blue);

        assertEquals(0, blue.getMoney());
        assertEquals(600, red.getMoney());
        assertEquals(1_000, yellow.getMoney());
    }

    /**
     * Debt is added to the debt the next player already has.
     */
    @Test
    void addsToExistingDebt() {
        setMoney(red, -200);
        setMoney(yellow, -100);

        play(card, red);

        assertEquals(-500, yellow.getMoney());
    }

    /**
     * A player with money can't play the card, and nothing changes.
     */
    @Test
    void failsWithPositiveBalance() {
        setMoney(red, 1);

        assertThrows(InvalidActionException.class, () -> play(card, red));
        assertEquals(1, red.getMoney());
        assertEquals(1_000, yellow.getMoney());
    }
}
