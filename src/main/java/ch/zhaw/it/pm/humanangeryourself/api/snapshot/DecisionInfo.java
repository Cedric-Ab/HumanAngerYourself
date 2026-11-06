package ch.zhaw.it.pm.humanangeryourself.api.snapshot;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;

import java.util.List;

/**
 * Information about which actions a player can take.
 *
 * @param player           the player for which the decision information is relevant
 * @param availableActions representation of the actions the player can take
 */
public record DecisionInfo(
        PlayerColor player,
        List<? extends Action> availableActions
) { }
