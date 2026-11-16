package ch.zhaw.it.pm.humanangeryourself.api;

/**
 * Where a piece currently is on the board.
 */
public enum PieceState {
    /** The piece is on one of its home tiles and has not entered the track yet. */
    HOME,

    /** The piece is on the main track. */
    TRACK,

    /** The piece is on the staircase of its own color, on the way to the finish. */
    STAIRCASE,

    /** The piece has reached the finish area. */
    FINISHED
}
