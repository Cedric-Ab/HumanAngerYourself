package ch.zhaw.it.pm.humanangeryourself.domain.model;

import java.util.Objects;

/**
 * A piece of a player. Every piece starts on its own home tile.
 */
public class Piece {
    private final Tile homeTile;
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

    public Tile getCurrentTile() {
        return currentTile;
    }

    public Tile getHomeTile() {
        return homeTile;
    }
}
