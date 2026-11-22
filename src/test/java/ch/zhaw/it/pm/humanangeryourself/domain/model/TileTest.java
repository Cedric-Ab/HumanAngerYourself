package ch.zhaw.it.pm.humanangeryourself.domain.model;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link Tile}.
 */
class TileTest {

    /**
     * Properties can only be built on normal track tiles.
     */
    @ParameterizedTest
    @EnumSource(value = TileType.class, names = "TRACK", mode = EnumSource.Mode.EXCLUDE)
    void propertyOnlyOnTrack(TileType type) {
        Property property = new Property(PropertySize.SMALL);

        assertThrows(IllegalArgumentException.class, () -> new Tile(1, type, 0, property, null));
    }

    /**
     * A tile can be bought until someone owns its property.
     */
    @Test
    void buyableUntilOwned() {
        Property property = new Property(PropertySize.SMALL);
        Tile tile = new Tile(1, TileType.TRACK, 0, property, null);
        assertTrue(tile.isBuyable());

        property.setOwner(new Player("Alice", PlayerColor.RED, 0, List.of()));
        assertFalse(tile.isBuyable());
    }

    /**
     * The generated board has every tile once, with a safe tile every five steps.
     */
    @Test
    void boardLayoutIsCorrect() {
        List<Tile> tiles = Tile.createAll();

        // 60 track tiles + 1 finish + 4 colours * (4 home + 10 staircase)
        assertEquals(117, tiles.size());
        // Board rejects duplicate ids, so this also checks that all ids are unique
        Board board = new Board(tiles);
        for (int id = 1; id <= 60; id++) {
            TileType expected = id % 5 == 0 ? TileType.SAFE : TileType.TRACK;
            assertEquals(expected, board.getTileById(id).getType(), "tile " + id);
        }
    }
}
