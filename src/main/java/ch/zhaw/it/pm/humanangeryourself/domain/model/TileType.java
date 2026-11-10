package ch.zhaw.it.pm.humanangeryourself.domain.model;

/**
 * Types of tiles on the board.
 */
public enum TileType {
    /** A regular tile on the main track. */
    TRACK,

    /** A safe tile on the main track, where pieces cannot be sent back home. */
    SAFE,

    /** A home tile, where a player's pieces start and return to when sent back. */
    HOME,

    /** A tile of a finish lane that leads towards the finish area. */
    STAIRCASE,

    /** The finish area in the middle of the board, where pieces end the game. */
    FINISH_AREA
}
