package ch.zhaw.it.pm.humanangeryourself.domain.model;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link Board}.
 */
class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(Map.of());
    }

    /**
     * Places a small property of the given player on a tile of the board.
     */
    private Property placeProperty(int tileId, Player owner) {
        Property property = new Property(owner, PropertySize.SMALL);
        board.getTileById(tileId).setProperty(property);
        return property;
    }

    /**
     * The generated board has all tiles, with a safe tile every five steps.
     */
    @Test
    void boardLayoutIsCorrect() {
        // 60 track tiles + 1 finish + 4 colours * (4 home + 10 staircase)
        assertEquals(117, board.getTiles().size());
        for (int id = 1; id <= 60; id++) {
            TileType expected = id % 5 == 0 ? TileType.SAFE : TileType.TRACK;
            assertEquals(expected, board.getTileById(id).getType(), "tile " + id);
        }
    }

    /**
     * Every tile id exists only once.
     */
    @Test
    void tileIdsAreUnique() {
        long distinctIds = board.getTiles().stream().map(Tile::getId).distinct().count();

        assertEquals(board.getTiles().size(), distinctIds);
    }

    /**
     * Asking for a tile that doesn't exist fails.
     */
    @Test
    void unknownTileIdFails() {
        assertThrows(IllegalArgumentException.class, () -> board.getTileById(999));
    }

    /**
     * Each quarter contains its own 15 track, 4 home and 10 staircase tiles.
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    void quarterHasOnlyOwnTiles(int quarter) {
        List<Tile> tiles = board.getTilesOfQuarter(quarter);

        assertEquals(29, tiles.size());
        assertTrue(tiles.stream().allMatch(tile -> tile.getQuarter() == quarter));
    }

    /**
     * Colors are arranged around the board in a fixed order.
     */
    @ParameterizedTest
    @CsvSource({"RED, 0", "YELLOW, 1", "BLUE, 2", "GREEN, 3"})
    void colorHasItsQuarter(PlayerColor color, int quarter) {
        assertEquals(quarter, board.getQuarterOf(color));
    }

    /**
     * Each color has its own four home tiles.
     */
    @ParameterizedTest
    @CsvSource({"RED, 101", "YELLOW, 201", "BLUE, 301", "GREEN, 401"})
    void colorHasItsHomeTiles(PlayerColor color, int firstId) {
        List<Tile> homeTiles = board.getHomeTiles(color);

        assertEquals(List.of(firstId, firstId + 1, firstId + 2, firstId + 3),
                homeTiles.stream().map(Tile::getId).toList());
        assertTrue(homeTiles.stream().allMatch(tile -> tile.getType() == TileType.HOME));
    }

    /**
     * Only the player's own properties are returned.
     */
    @Test
    void findsOnlyOwnProperties() {
        Player alice = new Player("Alice", PlayerColor.RED, 0, List.of());
        Player bob = new Player("Bob", PlayerColor.BLUE, 0, List.of());
        Property first = placeProperty(1, alice);
        placeProperty(2, bob);
        Property second = placeProperty(3, alice);

        assertEquals(List.of(first, second), board.getPropertiesOfPlayer(alice));
    }

    /**
     * NPCs stand on the configured tile and nowhere else.
     */
    @Test
    void placesNpcOnItsTile() {
        Npc npc = new Npc("Trader", List.of());

        Board boardWithNpc = new Board(Map.of(10, npc));

        assertEquals(List.of(boardWithNpc.getTileById(10)), boardWithNpc.getTiles().stream()
                .filter(tile -> tile.getNpc().isPresent())
                .toList());
        assertSame(npc, boardWithNpc.getTileById(10).getNpc().orElseThrow());
    }

    /**
     * NPCs can't be placed on track, finish, home, staircase or unknown tiles.
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 100, 101, 105, 999})
    void rejectsMisplacedNpc(int tileId) {
        Map<Integer, Npc> npcs = Map.of(tileId, new Npc("Trader", List.of()));

        assertThrows(IllegalArgumentException.class, () -> new Board(npcs));
    }
}
