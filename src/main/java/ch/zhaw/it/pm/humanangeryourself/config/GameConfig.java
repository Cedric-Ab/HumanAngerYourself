package ch.zhaw.it.pm.humanangeryourself.config;

import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

/**
 * Immutable game settings that control the economy and the card limits.
 */
public class GameConfig {
    /** The single instance, {@code null} until the configuration is initialized. */
    private static GameConfig instance;

    /** Money each player starts with. */
    private final long startMoney;

    /** Maximum debt before an auction is triggered. */
    private final long debtLimit;

    /** Maximum number of cards a player can hold. */
    private final int maxHandSize;

    /** Maximum number of properties per board quarter. */
    private final int maxPropertiesPerQuarter;

    /** Gambling settings. */
    private final GamblingConfig gambling;

    /** Price and rent of each property size. */
    private final Map<PropertySize, PropertyPrice> propertyPrices;

    /**
     * Creates a configuration from already validated values.
     *
     * @param startMoney              money each player starts with
     * @param debtLimit               maximum debt before an auction is triggered
     * @param maxHandSize             maximum number of cards a player can hold
     * @param maxPropertiesPerQuarter maximum number of properties per board quarter
     * @param gambling                gambling settings
     * @param propertyPrices          price and rent per property size, copied defensively
     */
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
     * @param propertyPrices          price and rent per property size
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
     * Initializes the configuration with the default values. Like {@link #initialize}, this
     * sets the singleton instance, so it can only be called once.
     *
     * @return the newly created {@code GameConfig} with default values
     * @throws IllegalStateException if an instance has already been initialized
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

    /**
     * Returns the money each player starts with.
     *
     * @return the start money
     */
    public long startMoney() {
        return startMoney;
    }

    /**
     * Returns the maximum debt a player can have before an auction is triggered.
     *
     * @return the debt limit
     */
    public long debtLimit() {
        return debtLimit;
    }

    /**
     * Returns the maximum number of cards a player can hold.
     *
     * @return the maximum hand size
     */
    public int maxHandSize() {
        return maxHandSize;
    }

    /**
     * Returns the maximum number of properties per board quarter.
     *
     * @return the maximum number of properties per quarter
     */
    public int maxPropertiesPerQuarter() {
        return maxPropertiesPerQuarter;
    }

    /**
     * Returns the gambling settings.
     *
     * @return the gambling configuration
     */
    public GamblingConfig gambling() {
        return gambling;
    }

    /**
     * Returns the price and rent of each property size.
     *
     * @return an unmodifiable map from property size to its price and rent
     */
    public Map<PropertySize, PropertyPrice> propertyPrices() {
        return propertyPrices;
    }
}
