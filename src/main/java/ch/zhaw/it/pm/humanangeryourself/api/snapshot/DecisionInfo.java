package ch.zhaw.it.pm.humanangeryourself.api.snapshot;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;

import java.util.List;

/**
 * Information about which actions the decider can take.
 *
 * @param decider          the player who has to decide
 * @param availableActions representation of the actions the player can take
 */
public record DecisionInfo(
        PlayerColor decider,
        List<? extends Action> availableActions
) { }
