package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.config.GamblingConfig;

import java.util.Random;

/**
 * Defines the rules for the gambling mechanic.
 * Players have a 60% chance to win and a 40% chance to lose
 * a random amount between 1,000 and 100,000.
 */
public class GamblingRules {
    private static final Random RANDOM = new Random();

    private GamblingConfig config;

    /**
     * Creates a new instance of the gambling rules.
     */
    public GamblingRules() {
    }

    /**
     * Injects the gambling configuration into the gambling rules, allowing it to access
     *
     * @param config the gambling configuration to be used for gambling calculations
     */
    public void inject(GamblingConfig config) {
        this.config = config;
    }

    /**
     * Simulates a gambling round and determines the outcome.
     *
     * @return a positive amount between 1,000 and 100,000 if the player wins,
     *         or a negative amount between -1,000 and -100,000 if the player loses
     */
    public long spin() {
        int chance = RANDOM.nextInt(100);

        if (chance < 60) {
            return RANDOM.nextLong(config.amountLowerBound(), config.amountUpperBound());
        }

        return -RANDOM.nextLong(config.amountLowerBound(), config.amountUpperBound());
    }


    // TODO implement spin method to receive Action Cards
}
