package ch.zhaw.it.pm.humanangeryourself.domain.model;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link Board}.
 */
class BoardTest {

    /**
     * Creates a track tile with a small property owned by the given player (or nobody).
     */
    private static Tile propertyTile(int id, Player owner) {
        Property property = new Property(PropertySize.SMALL);
        property.setOwner(owner);
        return new Tile(id, TileType.TRACK, 0, property, null);
    }

    /**
     * Two tiles with the same id are not allowed.
     */
    @Test
    void rejectsDuplicateIds() {
        List<Tile> tiles = List.of(propertyTile(1, null), propertyTile(1, null));

        assertThrows(IllegalArgumentException.class, () -> new Board(tiles));
    }

    /**
     * Asking for a tile that doesn't exist fails.
     */
    @Test
    void unknownTileIdFails() {
        Board board = new Board(List.of(propertyTile(1, null)));

        assertThrows(IllegalArgumentException.class, () -> board.getTileById(999));
    }

    /**
     * Tiles from outside the board can't be bought.
     */
    @Test
    void foreignTileNotBuyable() {
        Board board = new Board(List.of(propertyTile(1, null)));

        assertFalse(board.isBuyable(propertyTile(2, null)));
    }

    /**
     * Only the player's own properties are returned.
     */
    @Test
    void findsOnlyOwnProperties() {
        Player alice = new Player("Alice", PlayerColor.RED, 0, List.of());
        Player bob = new Player("Bob", PlayerColor.BLUE, 0, List.of());
        Board board = new Board(List.of(
                propertyTile(1, alice),
                propertyTile(2, bob),
                propertyTile(3, alice),
                propertyTile(4, null)));

        assertEquals(2, board.getPropertiesOfPlayer(alice).size());
    }
}
