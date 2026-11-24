package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult.GambleResult.MoneyResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link MoneyTigerCard}.
 */
class MoneyTigerCardTest extends CardTestBase {

    private final MoneyTigerCard card = new MoneyTigerCard();

    @BeforeEach
    void setUp() {
        when(gamblingRules.spinWin()).thenReturn(new MoneyResult(400));
        setMoney(red, 1_000);
        setMoney(yellow, 1_000);
    }

    /**
     * The player receives twice the payout, the others nothing.
     */
    @Test
    void paysDoubledWin() {
        play(card, red);

        assertEquals(1_800, red.getMoney());
        assertEquals(1_000, yellow.getMoney());
    }

    /**
     * The turn shows the doubled payout as result.
     */
    @Test
    void storesDoubledWinAsResult() {
        play(card, red);

        assertEquals(new MoneyResult(800), game.getTurn().getPrimaryActionResult());
    }
}
