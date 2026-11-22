package ch.zhaw.it.pm.humanangeryourself.config;

/**
 * Settings for gambling.
 *
 * @param winWeight  weight of the outcome "win money"
 * @param lossWeight weight of the outcome "lose money"
 * @param cardWeight weight of the outcome "receive a card"
 * @param amountUpperBound  money a player winds
 * @param amountLowerBound money a players loses
 */
public record GamblingConfig(
        int winWeight,
        int lossWeight,
        int cardWeight,
        long amountUpperBound,
        long amountLowerBound
        // Map<CardType, Integer> cardWeights
) {
    /**
     * Creates a gambling configuration and validates its values.
     *
     * @throws IllegalArgumentException if a weight or amount is negative, or if all three weights are zero
     */
    public GamblingConfig {
        if (winWeight < 0 || lossWeight < 0 || cardWeight < 0
                || winWeight + lossWeight + cardWeight == 0
                || amountUpperBound < 0 || amountLowerBound < 0) {
            throw new IllegalArgumentException("Invalid GamblingConfig");
        }
    }

    /**
     * Returns the default gambling configuration: a 48% chance to win money, a 32% chance to
     * lose money and a 20% chance to receive a card.
     * <p>
     * @return the default configuration
     */
    public static GamblingConfig defaults() {
        return new GamblingConfig(48, 32, 20, 100000, 1000);
    }
}
