package ch.zhaw.it.pm.humanangeryourself.domain.model;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link Tile}.
 */
class TileTest {

    private final Player alice = new Player("Alice", PlayerColor.RED, 0, List.of());
    private final Property property = new Property(alice, PropertySize.SMALL);
    private Tile tile;

    @BeforeEach
    void setUp() {
        tile = new Tile(1, TileType.TRACK, 0, null);
    }

    /**
     * A property placed on a track tile can be found there.
     */
    @Test
    void keepsPlacedProperty() {
        tile.setProperty(property);

        assertSame(property, tile.getProperty().orElseThrow());
    }

    /**
     * A second property can't replace the first one.
     */
    @Test
    void rejectsSecondProperty() {
        tile.setProperty(property);
        Property second = new Property(alice, PropertySize.LARGE);

        assertThrows(IllegalStateException.class, () -> tile.setProperty(second));
        assertSame(property, tile.getProperty().orElseThrow());
    }

    /**
     * A missing property can't be placed, removing has its own method.
     */
    @Test
    void rejectsNullProperty() {
        assertThrows(NullPointerException.class, () -> tile.setProperty(null));
    }

    /**
     * After removing a property, the tile is free for a new one.
     */
    @Test
    void removedPropertyFreesTile() {
        tile.setProperty(property);

        tile.removeProperty();

        assertTrue(tile.getProperty().isEmpty());
        tile.setProperty(property);
        assertSame(property, tile.getProperty().orElseThrow());
    }

    /**
     * Removing a property from an empty tile fails.
     */
    @Test
    void removingMissingPropertyFails() {
        assertThrows(IllegalStateException.class, tile::removeProperty);
    }

    /**
     * Pieces can only be hit on normal track tiles.
     */
    @ParameterizedTest
    @EnumSource(TileType.class)
    void onlyTrackIsNotSafe(TileType type) {
        assertEquals(type != TileType.TRACK, new Tile(1, type, 0, null).isSafe());
    }

    /**
     * An NPC on a safe tile can be found there, other tiles don't have one.
     */
    @Test
    void safeTileKeepsNpc() {
        Npc npc = new Npc("Trader", List.of());

        assertSame(npc, new Tile(5, TileType.SAFE, 0, npc).getNpc().orElseThrow());
        assertTrue(tile.getNpc().isEmpty());
    }

    /**
     * NPCs can't stand anywhere else than on safe tiles.
     */
    @ParameterizedTest
    @EnumSource(value = TileType.class, mode = EnumSource.Mode.EXCLUDE, names = "SAFE")
    void rejectsNpcOnOtherTiles(TileType type) {
        Npc npc = new Npc("Trader", List.of());

        assertThrows(IllegalArgumentException.class, () -> new Tile(1, type, 0, npc));
    }
}
