package ch.zhaw.it.pm.humanangeryourself.domain.model;

import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;
import ch.zhaw.it.pm.humanangeryourself.config.GameConfig;
import ch.zhaw.it.pm.humanangeryourself.config.PropertyPrice;

import java.util.Objects;
import java.util.Optional;

/**
 * A property that can be bought by a player. Price and rent depend on its {@link PropertySize}.
 */
public class Property {
    /** Size of the property, which determines its price and rent. */
    private final PropertySize size;

    /** Current owner, or {@code null} if nobody owns the property. */
    private Player owner;

    /**
     * Creates a new property without an owner.
     *
     * @param size size of the property
     * @throws NullPointerException if {@code size} is {@code null}
     */
    public Property(PropertySize size) {
        this.size = Objects.requireNonNull(size, "size must not be null");
    }

    /**
     * Returns the purchase price of this property.
     *
     * @param config game configuration that contains the prices
     * @return the purchase price
     */
    public long getPrice(GameConfig config) {
        return priceOf(config).price();
    }

    /**
     * Returns the rent other players pay when landing on this property.
     *
     * @param config game configuration that contains the prices
     * @return the rent
     */
    public long getRent(GameConfig config) {
        return priceOf(config).rent();
    }

    /**
     * Looks up the price entry for this property's size in the given configuration.
     *
     * @param config game configuration that contains the prices
     * @return the price entry for this property's size
     * @throws NullPointerException  if {@code config} is {@code null}
     * @throws IllegalStateException if no price is configured for this property's size
     */
    private PropertyPrice priceOf(GameConfig config) {
        Objects.requireNonNull(config, "config must not be null");
        PropertyPrice price = config.propertyPrices().get(size);
        if (price == null) {
            throw new IllegalStateException("No price configured for property size " + size);
        }
        return price;
    }

    /**
     * Returns the size of this property.
     *
     * @return the property size, never {@code null}
     */
    public PropertySize getSize() {
        return size;
    }

    /**
     * Returns the owner of this property.
     *
     * @return the owner, or an empty Optional if nobody owns the property
     */
    public Optional<Player> getOwner() {
        return Optional.ofNullable(owner);
    }

    /**
     * Sets the owner of this property.
     *
     * @param owner new owner, or {@code null} to make the property available again (e.g. after bankruptcy)
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
