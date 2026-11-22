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

        if (chance <= 20) {
            return new SpinResult(receiveCard());
        } else if (chance <= 32) {

            long amount = -RANDOM.nextLong(config.amountLowerBound(), config.amountUpperBound());
            return new SpinResult(amount);
        } else {
            long amount = RANDOM.nextLong(config.amountLowerBound(), config.amountUpperBound());
            return new SpinResult(amount);
        }

    }


    public CardType receiveCard() {
        List<CardType> cards = CardType.all();
        return cards.get(RANDOM.nextInt(cards.size()));
    }
}
