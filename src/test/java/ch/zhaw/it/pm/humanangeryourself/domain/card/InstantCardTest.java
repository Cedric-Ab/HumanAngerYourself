package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.config.CardConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the instant cards {@link JackpotCard} and {@link CharityCard}.
 */
class InstantCardTest extends CardTestBase {

    private static final CardConfig CONFIG = new CardConfig(500, -300, 10);

    /**
     * The jackpot pays the configured amount, whatever the player owned before.
     */
    @ParameterizedTest
    @ValueSource(longs = {-200, 0, 1_000})
    void jackpotPaysConfiguredAmount(long money) {
        setMoney(red, money);

        new JackpotCard(CONFIG).apply(game, red);

        assertEquals(money + 500, red.getMoney());
    }

    /**
     * Charity takes the configured amount, even from players without money.
     */
    @ParameterizedTest
    @ValueSource(longs = {-200, 0, 1_000})
    void charityTakesConfiguredAmount(long money) {
        setMoney(red, money);

        new CharityCard(CONFIG).apply(game, red);

        assertEquals(money - 300, red.getMoney());
    }

    /**
     * Cards without their own configuration use the default amounts.
     */
    @Test
    void usesDefaultAmounts() {
        setMoney(red, 0);
        setMoney(yellow, 0);

        new JackpotCard().apply(game, red);
        new CharityCard().apply(game, yellow);

        assertEquals(CardConfig.defaults().jackpot(), red.getMoney());
        assertEquals(CardConfig.defaults().charity(), yellow.getMoney());
    }

    /**
     * Only the player who drew the card is affected.
     */
    @Test
    void othersAreNotAffected() {
        setMoney(yellow, 1_000);

        new JackpotCard(CONFIG).apply(game, red);
        assertEquals(1_000, yellow.getMoney());

        new CharityCard(CONFIG).apply(game, red);
        assertEquals(1_000, yellow.getMoney());
    }
}
