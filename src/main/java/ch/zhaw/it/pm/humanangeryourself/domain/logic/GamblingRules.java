package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.config.GamblingConfig;

import java.util.List;
import java.util.Random;

/**
 * Defines the rules for the gambling mechanic.
 * Players have a 60% chance to win and a 40% chance to lose
 * a random amount between 1,000 and 100,000.
 */
public class GamblingRules {
    private static final Random RANDOM = new Random();

    private final GamblingConfig config;

    /**
     * Creates a new instance of the gambling rules with the default configuration.
     */
    public GamblingRules() {
        this(GamblingConfig.defaults());
    }

    /**
     * Creates a new instance of the gambling rules with a custom configuration.
     *
     * @param config the custom configuration to use
     */
    public GamblingRules(GamblingConfig config) { this.config = config; }

    /**
     * Simulates a gambling round and determines the outcome.
     *
     * @return a positive amount between 1,000 and 100,000 if the player wins,
     *         or a negative amount between -1,000 and -100,000 if the player loses
     *         or a CardType
     */
    public SpinResult spin() {
        int chance = RANDOM.nextInt(100);

        if (chance <= config.cardWeight() - 1) {
            return spinCard();
        } else if (chance <= config.lossWeight() + config.cardWeight() - 1) {
            long amount = -RANDOM.nextLong(config.amountLowerBound(), config.amountUpperBound());
            return new SpinResult(amount);
        } else {
            return spinWin();
        }
    }

    public SpinResult spinCard() {
        return new SpinResult(receiveCard());
    }

    public SpinResult spinWin() {
        long amount = RANDOM.nextLong(config.amountLowerBound(), config.amountUpperBound());
        return new SpinResult(amount);
    }
    public CardType receiveCard() {
        List<CardType> cards = CardType.all();
        return cards.get(RANDOM.nextInt(cards.size()));
    }
}
