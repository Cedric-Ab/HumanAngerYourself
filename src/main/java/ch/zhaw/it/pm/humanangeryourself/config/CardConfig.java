package ch.zhaw.it.pm.humanangeryourself.config;

/**
 * Holds the configurable values for the card mechanics of the game.
 *
 * <p>This is an immutable configuration record. Use {@link #defaults()} to obtain
 * the standard game settings.</p>
 *
 * @param jackpot     the amount awarded when a jackpot card is drawn
 * @param charity     the amount deducted when a charity card is drawn
 * @param maxHandSize the maximum number of cards a player may hold in their hand
 */
public record CardConfig(
        int jackpot,
        int charity,
        int maxHandSize
) {
    /**
     * Creates a {@code CardConfig} with the default game settings.
     *
     * <p>The defaults are a jackpot of 250,000, a charity of -150,000 and a
     * maximum hand size of 10 cards.</p>
     *
     * @return a new {@code CardConfig} containing the default values
     */
    public static CardConfig defaults() {
        return new CardConfig(250000, -150000, 10);
    }
}

