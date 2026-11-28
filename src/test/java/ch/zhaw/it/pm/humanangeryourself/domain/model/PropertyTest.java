package ch.zhaw.it.pm.humanangeryourself.domain.model;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;
import ch.zhaw.it.pm.humanangeryourself.config.GameConfig;
import ch.zhaw.it.pm.humanangeryourself.config.PropertyPrice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link Property}.
 */
class PropertyTest {

    private final Player alice = new Player("Alice", PlayerColor.RED, 0, List.of());

    /**
     * Creates a game configuration with the given property prices.
     */
    private static GameConfig configWith(Map<PropertySize, PropertyPrice> prices) {
        GameConfig defaults = GameConfig.defaults();
        return new GameConfig(defaults.startMoney(), defaults.debtLimit(), defaults.maxHandSize(),
                defaults.maxPropertiesPerQuarter(), defaults.gambling(), defaults.cards(), prices, List.of());
    }

    /**
     * Price and rent depend on the property's size.
     */
    @ParameterizedTest
    @CsvSource({
            "SMALL,  100, 10",
            "MEDIUM, 200, 20",
            "LARGE,  500, 50"
    })
    void priceAndRentBySize(PropertySize size, long price, long rent) {
        GameConfig config = configWith(Map.of(
                PropertySize.SMALL, new PropertyPrice(100, 10),
                PropertySize.MEDIUM, new PropertyPrice(200, 20),
                PropertySize.LARGE, new PropertyPrice(500, 50)));
        Property property = new Property(alice, size);

        assertEquals(price, property.getPrice(config));
        assertEquals(rent, property.getRent(config));
    }

    /**
     * A size without a configured price fails instead of returning nonsense.
     */
    @Test
    void missingPriceFails() {
        GameConfig config = configWith(Map.of(PropertySize.SMALL, new PropertyPrice(1, 1)));
        Property property = new Property(alice, PropertySize.LARGE);

        assertThrows(IllegalStateException.class, () -> property.getPrice(config));
    }

    /**
     * A property can't exist without an owner.
     */
    @Test
    void ownerIsRequired() {
        assertThrows(NullPointerException.class, () -> new Property(null, PropertySize.SMALL));
    }

    /**
     * A property can't exist without a size.
     */
    @Test
    void sizeIsRequired() {
        assertThrows(NullPointerException.class, () -> new Property(alice, null));
    }
}
