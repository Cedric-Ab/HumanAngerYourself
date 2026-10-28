package ch.zhaw.it.pm.humanangeryourself.api.snapshot;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;

/**
 * General information about one player. Only what other players are allowed to see.
 *
 * @param name      player's name
 * @param color     player's color; used for identification
 * @param money     how much money this player owns (can be negative)
 * @param cardCount number of cards the player has
 */
public record PlayerInfo(
        String name,
        PlayerColor color,
        long money,
        int cardCount
) {
}
