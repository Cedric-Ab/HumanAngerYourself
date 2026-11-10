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
    private final PropertySize size;
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

    private PropertyPrice priceOf(GameConfig config) {
        Objects.requireNonNull(config, "config must not be null");
        PropertyPrice price = config.propertyPrices().get(size);
        if (price == null) {
            throw new IllegalStateException("No price configured for property size " + size);
        }
        return price;
    }

    public PropertySize getSize() {
        return size;
    }

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
