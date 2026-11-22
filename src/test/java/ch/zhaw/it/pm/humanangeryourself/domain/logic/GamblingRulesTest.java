package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.config.GamblingConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link GamblingRules}.
 */
class GamblingRulesTest {

    /**
     * Won or lost amounts always stay within the configured limits.
     */
    @Test
    void amountStaysWithinLimits() {
        GamblingRules rules = new GamblingRules(new GamblingConfig(1, 1, 1, 500L, 10L));

        for (int i = 0; i < 1000; i++) {
            long amount = Math.abs(rules.spin());
            assertTrue(amount >= 10 && amount < 500, "amount out of range: " + amount);
        }
    }

    /**
     * Over many spins, players both win and lose money.
     */
    @Test
    void canWinAndLose() {
        GamblingRules rules = new GamblingRules();
        boolean won = false;
        boolean lost = false;

        for (int i = 0; i < 1000; i++) {
            long amount = rules.spin();
            won |= amount > 0;
            lost |= amount < 0;
        }

        assertTrue(won && lost);
    }
}
