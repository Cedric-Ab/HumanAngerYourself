package ch.zhaw.it.pm.humanangeryourself.config;

import java.util.Objects;

/**
 * Immutable game settings that control the economy and the card limits.
 *
 * @param startMoney              money each player starts with
 * @param debtLimit               maximum debt before an auction is triggered
 * @param maxHandSize             maximum number of cards a player can hold
 * @param maxPropertiesPerQuarter maximum number of oprierties per board quarter
 * @param gambling                gambling settings
 */
public record GameConfig(
        long startMoney,
        long debtLimit,
        int maxHandSize,
        int maxPropertiesPerQuarter,
        GamblingConfig gambling
        // Map<PropertySize, PropertyPrice> propertyPrices
) {
    /**
     * Validates the configuration values.
     *
     * @throws IllegalArgumentException if a value is negative or a limit is below 1
     */
    public GameConfig {
        Objects.requireNonNull(gambling, "gambling must be present");
        // propertyPrices = Map.copyof(propertyPrices);
        if (startMoney < 0 || debtLimit < 0 || maxHandSize < 1 || maxPropertiesPerQuarter < 1) {
            throw new IllegalArgumentException("GameConfig invalid");
        }
    }

    /**
     * Creates a configuration with the default values.
     *
     * @return a new {@code GameConfig} with default values.
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    public static GameConfig defaults() {
        return new GameConfig(100_000, 50_000, 10, 5, GamblingConfig.defaults());
    }
}
