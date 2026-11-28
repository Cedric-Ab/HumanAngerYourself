package ch.zhaw.it.pm.humanangeryourself.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link Piece}.
 */
class PieceTest {

    private final Tile home = new Tile(101, TileType.HOME, 0, null);
    private final Tile track = new Tile(1, TileType.TRACK, 0, null);
    private Piece piece;

    @BeforeEach
    void setUp() {
        piece = new Piece(0, home);
    }

    /**
     * A new piece stands on its home tile.
     */
    @Test
    void startsAtHome() {
        assertSame(home, piece.getCurrentTile());
    }

    /**
     * Moving changes the current tile, but not the home tile.
     */
    @Test
    void movesToTile() {
        piece.moveTo(track);

        assertSame(track, piece.getCurrentTile());
        assertSame(home, piece.getHomeTile());
    }

    /**
     * A piece that is sent home stands on its home tile again.
     */
    @Test
    void returnsHome() {
        piece.moveTo(track);

        piece.sendHome();

        assertSame(home, piece.getCurrentTile());
    }

    /**
     * A piece can't move to a missing tile and stays where it is.
     */
    @Test
    void rejectsNullTile() {
        assertThrows(NullPointerException.class, () -> piece.moveTo(null));
        assertSame(home, piece.getCurrentTile());
    }

    /**
     * A piece can't exist without a home tile.
     */
    @Test
    void homeTileIsRequired() {
        assertThrows(NullPointerException.class, () -> new Piece(0, null));
    }
}
