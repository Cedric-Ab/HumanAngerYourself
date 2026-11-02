package ch.zhaw.it.pm.humanangeryourself.api.snapshot;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Information about which actions a player can take.
 *
 * @param player           the player for which the decision information is relevant
 * @param availableActions representation of the actions the player can take
 */
public record DecisionInfo(
        @NotNull PlayerColor player,
        @NotNull List<? extends @NotNull Action> availableActions
) {
    public DecisionInfo(Player player, List<? extends Action> availableActions) {
        this(player.getColor(), availableActions);
    }
}
