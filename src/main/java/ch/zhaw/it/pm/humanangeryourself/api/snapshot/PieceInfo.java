package ch.zhaw.it.pm.humanangeryourself.api.snapshot;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;

/**
 * Information about a game piece.
 *
 * @param color who owns the piece
 * @param tile  where the piece is located
 */
public record PieceInfo(
        PlayerColor color,
        int tile
) {
}
