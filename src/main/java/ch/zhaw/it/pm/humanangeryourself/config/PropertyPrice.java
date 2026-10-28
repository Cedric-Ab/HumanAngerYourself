package ch.zhaw.it.pm.humanangeryourself.config;

/**
 * Purchase price and rent of a property of one size.
 *
 * @param price cost to buy the property
 * @param rent  amount another player pays when landing on the property
 */
public record PropertyPrice(long price, long rent) {

    /**
     * Validates the values.
     *
     * @throws IllegalArgumentException if a value is negative
     */
    public PropertyPrice {
        if (price < 0 || rent < 0) {
            throw new IllegalArgumentException("Invalid PropertyPrice");
        }
    }
}
