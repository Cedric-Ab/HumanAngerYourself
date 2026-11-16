package ch.zhaw.it.pm.humanangeryourself.config;

/**
 * Settings for gambling.
 *
 * @param winWeight  weight of the outcome "win money"
 * @param lossWeight weight of the outcome "lose money"
 * @param cardWeight weight of the outcome "receive a card"
 * @param winAmount  money a player winds
 * @param lossAmount money a players loses
 */
public record GamblingConfig(
        int winWeight,
        int lossWeight,
        int cardWeight,
        long winAmount,
        long lossAmount
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
                || winAmount < 0 || lossAmount < 0) {
            throw new IllegalArgumentException("Invalid GamblingConfig");
        }
    }

    /**
     * Returns the default gambling configuration: a 40% chance to win 20,000, a 40% chance to
     * lose 15,000 and a 20% chance to receive a card.
     * <p>
     * The values are placeholders and are still to be adjusted.
     *
     * @return the default configuration
     */
    // TODO, change values to something meaningful
    public static GamblingConfig defaults() {
        return new GamblingConfig(40, 40, 20, 20_000, 15_000);
    }
}
