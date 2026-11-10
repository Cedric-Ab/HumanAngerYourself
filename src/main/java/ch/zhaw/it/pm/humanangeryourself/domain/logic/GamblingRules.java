package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import java.util.Random;

/**
 * Defines the rules for the gambling mechanic.
 * Players have a 60% chance to win and a 40% chance to lose
 * a random amount between 1,000 and 100,000.
 */
public class GamblingRules {
    private static final Random RANDOM = new Random();

    /**
     * Simulates a gambling round and determines the outcome.
     *
     * @return a positive amount between 1,000 and 100,000 if the player wins,
     * or a negative amount between -1,000 and -100,000 if the player loses
     */
    //TODO refactor to use percentage config from GamblingConfig class
    public int spin(){
        int chance = RANDOM.nextInt(100);

        if (chance < 60) {
            return RANDOM.nextInt(1_000, 100_001);
        }

        return -RANDOM.nextInt(1_000, 100_001);
    }

    //TODO implement spin method to receive Action Cards
}
