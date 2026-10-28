package ch.zhaw.it.pm.humanangeryourself.api.action;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;

/**
 * Represent the action where a player targets a piece (their own or an enemy's).
 *
 * @param targetPlayer player whose piece was targeted
 * @param targetTile   where the targeted piece is located
 */
public record TargetPiece(
        PlayerColor targetPlayer,
        int targetTile
) implements Action {
}
