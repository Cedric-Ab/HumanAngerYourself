package ch.zhaw.it.pm.humanangeryourself.domain.model;

import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;
import ch.zhaw.it.pm.humanangeryourself.config.CardConfig;
import ch.zhaw.it.pm.humanangeryourself.config.GamblingConfig;
import ch.zhaw.it.pm.humanangeryourself.config.GameConfig;
import ch.zhaw.it.pm.humanangeryourself.config.PropertyPrice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link Property}.
 */
class PropertyTest {

    /**
     * Price and rent depend on the property's size.
     */
    @ParameterizedTest
    @CsvSource({
            "SMALL,  100000, 3000",
            "MEDIUM, 200000, 7000",
            "LARGE,  500000, 20000"
    })
    void priceAndRentBySize(PropertySize size, long price, long rent) {
        Property property = new Property(size);

        assertEquals(price, property.getPrice(GameConfig.defaults()));
        assertEquals(rent, property.getRent(GameConfig.defaults()));
    }

    /**
     * A size without a configured price fails instead of returning nonsense.
     */
    @Test
    void missingPriceFails() {
        GameConfig config = new GameConfig(100_000, 50_000, 10, 5,
                GamblingConfig.defaults(), CardConfig.defaults(),
                Map.of(PropertySize.SMALL, new PropertyPrice(1, 1)));

        assertThrows(IllegalStateException.class, () -> new Property(PropertySize.LARGE).getPrice(config));
    }
}
