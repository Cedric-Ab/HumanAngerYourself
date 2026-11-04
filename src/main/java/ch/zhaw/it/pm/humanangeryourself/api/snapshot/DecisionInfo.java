package ch.zhaw.it.pm.humanangeryourself.api.snapshot;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;

import java.util.List;

/**
 * Information about which actions the current viewer can take.
 *
 * @param availableActions representation of the actions the player can take
 * @param decider          the player who has to decide
 */
public record DecisionInfo(List<Action> availableActions, PlayerColor decider) { }
