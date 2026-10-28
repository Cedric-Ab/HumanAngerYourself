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
// TODO currently as comment becuase of checkstyle.... @param cardWeights weight of each card type when a card is won
public record GamblingConfig(
        int winWeight,
        int lossWeight,
        int cardWeight,
        long winAmount,
        long lossAmount
        // Map<CardType, Integer> cardWeights
) {
    public GamblingConfig {
        if (winWeight < 0 || lossWeight < 0 || cardWeight < 0
                || winWeight + lossWeight + cardWeight == 0
                || winAmount < 0 || lossAmount < 0) {
            throw new IllegalArgumentException("Invalid GamblingConfig");
        }
    }

    // TODO, change values to something meaningful
    @SuppressWarnings("checkstyle:MagicNumber")
    public static GamblingConfig defaults() {
        return new GamblingConfig(40, 40, 20, 20_000, 15_000);
    }
}
