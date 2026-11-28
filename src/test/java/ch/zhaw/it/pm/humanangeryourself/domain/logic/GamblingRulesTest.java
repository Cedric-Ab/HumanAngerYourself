package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult.GambleResult;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult.GambleResult.CardResult;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult.GambleResult.MoneyResult;
import ch.zhaw.it.pm.humanangeryourself.config.GamblingConfig;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
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
        GamblingRules rules = new GamblingRules(new GamblingConfig(50, 50, 0, 500L, 10L));

        for (int i = 0; i < 1000; i++) {
            MoneyResult result = assertInstanceOf(MoneyResult.class, rules.spin());
            long amount = Math.abs(result.amount());
            assertTrue(amount >= 10 && amount < 500, "amount out of range: " + amount);
        }
    }

    /**
     * Over many spins, players win money, lose money and draw cards.
     */
    @Test
    void canWinLoseAndDrawCards() {
        GamblingRules rules = new GamblingRules();
        boolean won = false;
        boolean lost = false;
        boolean drewCard = false;

        for (int i = 0; i < 1000; i++) {
            GambleResult result = rules.spin();
            won |= result instanceof MoneyResult money && money.amount() > 0;
            lost |= result instanceof MoneyResult money && money.amount() < 0;
            drewCard |= result instanceof CardResult;
        }

        assertTrue(won && lost && drewCard);
    }

    /**
     * A guaranteed win pays out, even if normal spins are always lost.
     */
    @Test
    void spinWinAlwaysPays() {
        GamblingRules rules = new GamblingRules(new GamblingConfig(0, 100, 0, 500L, 10L));

        for (int i = 0; i < 1000; i++) {
            long amount = rules.spinWin().amount();
            assertTrue(amount >= 10 && amount < 500, "amount out of range: " + amount);
        }
    }

    /**
     * Every type of card can be drawn.
     */
    @Test
    void everyCardCanBeDrawn() {
        GamblingRules rules = new GamblingRules();

        Set<CardType> drawn = Stream.generate(() -> rules.spinCard().cardType())
                .limit(1000)
                .collect(Collectors.toSet());

        assertEquals(Set.of(CardType.values()), drawn);
    }
}
