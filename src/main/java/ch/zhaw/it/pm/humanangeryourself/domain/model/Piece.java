package ch.zhaw.it.pm.humanangeryourself.domain.model;

import java.util.Objects;

/**
 * A piece of a player. Every piece starts on its own home tile.
 */
public class Piece {
    /** Tile the piece starts on and returns to when it is sent home. */
    private final Tile homeTile;

    /** Tile the piece currently stands on. */
    private Tile currentTile;

    /**
     * Creates a new piece that is placed on its home tile.
     *
     * @param homeTile home tile of the piece
     * @throws NullPointerException if {@code homeTile} is {@code null}
     */
    public Piece(Tile homeTile) {
        this.homeTile = Objects.requireNonNull(homeTile, "homeTile must not be null");
        this.currentTile = homeTile;
    }

    /**
     * Places the piece on the given tile. Whether the move is legal is checked by the movement rules.
     *
     * @param tile target tile
     * @throws NullPointerException if {@code tile} is {@code null}
     */
    public void moveTo(Tile tile) {
        currentTile = Objects.requireNonNull(tile, "tile must not be null");
    }

    /**
     * Sends the piece back to its home tile.
     */
    public void sendHome() {
        currentTile = homeTile;
    }

    /**
     * Returns the tile the piece currently stands on.
     *
     * @return the current tile, never {@code null}
     */
    public Tile getCurrentTile() {
        return currentTile;
    }

    /**
     * Returns the home tile of the piece.
     *
     * @return the home tile, never {@code null}
     */
    public Tile getHomeTile() {
        return homeTile;
    }
}
