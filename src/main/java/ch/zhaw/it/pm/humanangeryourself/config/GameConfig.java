package ch.zhaw.it.pm.humanangeryourself.config;

import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;

import java.util.Map;
import java.util.Objects;

/**
 * Immutable game settings that control the economy and the card limits.
 *
 * @param startMoney              money each player starts with
 * @param maxHandSize             maximum number of cards a player can hold
 * @param maxPropertiesPerQuarter maximum number of properties per board quarter
 * @param gambling                gambling settings
 * @param cards                   ActionCard settings
 * @param propertyPrices          purchase price and rent for each size of property
 */
public record GameConfig(
        long startMoney,
        int maxHandSize,
        int maxPropertiesPerQuarter,
        GamblingConfig gambling,
        CardConfig cards,
        Map<PropertySize, PropertyPrice> propertyPrices
) {
    /**
     * Validates the configuration values.
     *
     * @throws IllegalArgumentException if a value is negative or a limit is below 1
     */
    public GameConfig {
        Objects.requireNonNull(gambling, "gambling must be present");
        Objects.requireNonNull(cards, "cards must be present");
        propertyPrices = Map.copyOf(propertyPrices);
        if (startMoney < 0 || maxHandSize < 1 || maxPropertiesPerQuarter < 1) {
            throw new IllegalArgumentException("GameConfig invalid");
        }
    }

    /**
     * Creates a configuration with the default values.
     *
     * @return a new {@code GameConfig} with default values.
     */
    public static GameConfig defaults() {
        Map<PropertySize, PropertyPrice> propertyPrices = Map.of(
                PropertySize.SMALL, new PropertyPrice(100_000, 3_000),
                PropertySize.MEDIUM, new PropertyPrice(200_000, 7_000),
                PropertySize.LARGE, new PropertyPrice(500_000, 20_000));

        return new GameConfig(100_000, 10, 5,
                GamblingConfig.defaults(), CardConfig.defaults(), propertyPrices);
    }
}
