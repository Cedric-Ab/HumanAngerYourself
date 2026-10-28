package ch.zhaw.it.pm.humanangeryourself.api.action;

/**
 * Represent the action where a player moves one of their pieces after throwing the die.
 *
 * @param startingTile where the piece is standing now. This does not uniquely identify
 *                     the piece, but since pieces with the same color and position are
 *                     indistinguishable, unique ID is not required.
 * @param targetTile   where the piece will go
 */
public record MovePiece(
        int startingTile,
        int targetTile
) implements Action {
}
