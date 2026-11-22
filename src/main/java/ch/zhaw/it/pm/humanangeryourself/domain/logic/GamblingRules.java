package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.config.GamblingConfig;

import java.util.List;
import java.util.Random;

/**
 * Defines the rules for the gambling mechanic.
 * A spin has one of three outcomes: the player receives a random card, loses a random amount
 * of money, or wins a random amount of money. The probabilities of the card and loss outcomes
 * are given by the {@link GamblingConfig} as percentages (card weight and loss weight); the
 * remaining probability is the chance to win. Win and loss amounts are drawn from the range
 * defined by the configuration's lower and upper bounds.
 */
public class GamblingRules {

    /** Source of randomness shared by all instances. */
    private static final Random RANDOM = new Random();

    /** The configuration defining the outcome weights and the amount bounds. */
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
     * The outcome is chosen according to the configured card and loss weights:
     * a card, a loss, or (for the remaining probability) a win.
     *
     * @return a {@link SpinResult} containing a random card if the player receives a card,
     *         a negative amount if the player loses, or a positive amount if the player wins.
     *         Amounts lie between the configured lower bound (inclusive) and upper bound
     *         (exclusive), negated in the case of a loss
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

    /**
     * Produces a card outcome without regard to the configured weights.
     *
     * @return a {@link SpinResult} containing a randomly chosen card
     * @see #receiveCard()
     */
    public SpinResult spinCard() {
        return new SpinResult(receiveCard());
    }

    /**
     * Produces a winning outcome without regard to the configured weights.
     *
     * @return a {@link SpinResult} containing a positive amount between the configured lower
     *         bound (inclusive) and upper bound (exclusive)
     */
    public SpinResult spinWin() {
        long amount = RANDOM.nextLong(config.amountLowerBound(), config.amountUpperBound());
        return new SpinResult(amount);
    }

    /**
     * Picks a random card type, each type being equally likely.
     *
     * @return a randomly chosen {@link CardType} out of {@link CardType#all()}
     */
    private CardType receiveCard() {
        List<CardType> cards = CardType.all();
        return cards.get(RANDOM.nextInt(cards.size()));
    }
}
