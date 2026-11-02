package ch.zhaw.it.pm.humanangeryourself.api.snapshot;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;

/**
 * Information about a property.
 *
 * @param tile where the property is located
 * @param owner who owns the property
 * @param size size of this property (determines price and rent)
 */
public record PropertyInfo(
        int tile,
        PlayerColor owner,
        PropertySize size
) { }
