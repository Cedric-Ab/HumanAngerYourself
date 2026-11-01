package ch.zhaw.it.pm.humanangeryourself.api.snapshot;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;

/**
 * Information about which actions the current viewer can take.
 *
 * @param decider the player who has to decide
 */
public record DecisionInfo(PlayerColor decider) { }