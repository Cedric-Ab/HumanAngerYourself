package ch.zhaw.it.pm.humanangeryourself.api.action;

import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;

/**
 * Represent the action where a player buys a property.
 *
 * @param tile the board tile where the property is located
 * @param size the size of the property (which determines the price)
 */
public record BuyProperty(
        int tile,
        PropertySize size
) implements Action {
}
