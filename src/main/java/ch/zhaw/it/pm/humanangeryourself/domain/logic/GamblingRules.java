package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult.GambleResult;
import ch.zhaw.it.pm.humanangeryourself.config.GamblingConfig;

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
     */
    // TODO refactor to use percentage config from GamblingConfig class
    public GambleResult spin() {
        int chance = RANDOM.nextInt(100);
        int amount = RANDOM.nextInt(1_000, 100_001);

        if (chance < 60) {
            return new GambleResult.MoneyResult(amount);
        } else {
            return new GambleResult.MoneyResult(-amount);
        }
    }

    // TODO implement spin method to receive Action Cards
}
