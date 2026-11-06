package ch.zhaw.it.pm.humanangeryourself.config;

import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

/**
 * Immutable game settings that control the economy and the card limits.
 */
public class GameConfig {
    private static GameConfig instance;

    private final long startMoney;
    private final long debtLimit;
    private final int maxHandSize;
    private final int maxPropertiesPerQuarter;
    private final GamblingConfig gambling;
    private final Map<PropertySize, PropertyPrice> propertyPrices;

    private GameConfig(
            long startMoney,
            long debtLimit,
            int maxHandSize,
            int maxPropertiesPerQuarter,
            GamblingConfig gambling,
            Map<PropertySize, PropertyPrice> propertyPrices) {
        this.startMoney = startMoney;
        this.debtLimit = debtLimit;
        this.maxHandSize = maxHandSize;
        this.maxPropertiesPerQuarter = maxPropertiesPerQuarter;
        this.gambling = gambling;
        this.propertyPrices = Map.copyOf(propertyPrices);
    }

    /**
     * Validates the attributes and initializes a {@code GameConfig} instance.
     *
     * @param startMoney              money each player starts with
     * @param debtLimit               maximum debt before an auction is triggered
     * @param maxHandSize             maximum number of cards a player can hold
     * @param maxPropertiesPerQuarter maximum number of properties per board quarter
     * @param gambling                gambling settings
     * @return the newly created instance
     * @throws IllegalStateException    if an instance has already been initialized
     * @throws IllegalArgumentException if a value is negative or a limit is below 1
     * @throws NullPointerException     if either gambling or propertyPrices is null
     */
    public static GameConfig initialize(
            long startMoney,
            long debtLimit,
            int maxHandSize,
            int maxPropertiesPerQuarter,
            GamblingConfig gambling,
            Map<PropertySize, PropertyPrice> propertyPrices) {
        if (instance != null) {
            throw new IllegalStateException("GameConfig instance has already been initialized");
        }

        Objects.requireNonNull(gambling, "gambling must be present");
        Objects.requireNonNull(propertyPrices, "propertyPrices must be present");
        if (startMoney < 0 || debtLimit < 0 || maxHandSize < 1 || maxPropertiesPerQuarter < 1) {
            throw new IllegalArgumentException("GameConfig invalid");
        }

        instance = new GameConfig(startMoney, debtLimit, maxHandSize, maxPropertiesPerQuarter, gambling, propertyPrices);
        return instance;
    }

    /**
     * Creates a configuration with the default values.
     *
     * @return a new {@code GameConfig} with default values.
     */
    public static GameConfig initializeDefaults() {
        Map<PropertySize, PropertyPrice> propertyDefaults = Map.of(
                PropertySize.SMALL, new PropertyPrice(100_000, 3_000),
                PropertySize.MEDIUM, new PropertyPrice(200_000, 7_000),
                PropertySize.LARGE, new PropertyPrice(300_000, 20_000));
        return initialize(100_000, 50_000, 10, 5, GamblingConfig.defaults(), propertyDefaults);
    }

    /**
     * Returns the instance of {@code GameConfig}, if it has previously been initialized.
     *
     * @return the instance
     * @throws IllegalStateException if the instance has not been initialized yet
     */
    public static @NotNull GameConfig getInstance() {
        if (instance == null) {
            throw new IllegalStateException("GameConfig instance has not been initialized");
        }
        return instance;
    }

    public long startMoney() {
        return startMoney;
    }

    public long debtLimit() {
        return debtLimit;
    }

    public int maxHandSize() {
        return maxHandSize;
    }

    public int maxPropertiesPerQuarter() {
        return maxPropertiesPerQuarter;
    }

    public GamblingConfig gambling() {
        return gambling;
    }

    public Map<PropertySize, PropertyPrice> propertyPrices() {
        return propertyPrices;
    }
}
