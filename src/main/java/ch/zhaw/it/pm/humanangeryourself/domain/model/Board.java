package ch.zhaw.it.pm.humanangeryourself.domain.model;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The game board with all its tiles.
 * <p>
 * Responsibilities:
 * </p>
 * <ul>
 *     <li>builds the standard board when it is created</li>
 *     <li>knows which quarter and which home tiles belong to each color</li>
 *     <li>finds tiles by id and answers questions about them, for example which properties a player owns</li>
 * </ul>
 * <p>
 * Tile ids (same numbers as the fx:ids "tile&lt;id&gt;" in BoardView.fxml):
 * </p>
 * <ul>
 *     <li>{@code 1 - 60}: main track, counter-clockwise, every 5th tile is a safe space</li>
 *     <li>{@code 100}: shared finish in the centre</li>
 *     <li>{@code X01 - X04}: home slots of color X</li>
 *     <li>{@code X05 - X14}: staircase of color X, X05 is next to the track, X14 next to the finish</li>
 * </ul>
 * <p>
 * Color X: 1 = red, 2 = yellow, 3 = blue, 4 = green (counter-clockwise, X - 1 is also the quarter).
 * </p>
 */
public class Board {
    private static final int QUARTER_COUNT = 4;
    private static final int TILES_PER_QUARTER = 15;
    private static final int SAFE_INTERVAL = 5;
    private static final int FINISH_TILE_ID = 100;
    private static final int COLOR_ID_BLOCK = 100;
    private static final int HOME_SLOT_COUNT = 4;
    private static final int STAIRCASE_LENGTH = 10;
    private static final int NO_QUARTER = -1;

    private final List<Tile> tiles = new ArrayList<>();

    /**
     * Creates the board.
     */
    public Board() {
        for (int quarter = 0; quarter < QUARTER_COUNT; quarter++) {
            addTrackTiles(quarter);
        }
        tiles.add(new Tile(FINISH_TILE_ID, TileType.FINISH_AREA, NO_QUARTER, null));
        for (int quarter = 0; quarter < QUARTER_COUNT; quarter++) {
            addHomeAndStaircaseTiles(quarter);
        }
        requireUniqueIds();
    }

    /**
     * Returns all properties owned by the given player.
     *
     * @param player owner to look for
     * @return list of the player's properties
     */
    public List<Property> getPropertiesOfPlayer(Player player) {
        return tiles.stream()
                .map(Tile::getProperty)
                .flatMap(Optional::stream)
                .filter(property -> property.getOwner() == player)
                .toList();
    }

    /**
     * Returns the quarter of the board that belongs to the given color.
     *
     * @param color color of a player
     * @return quarter of the color, from 0 to 3 in moving direction
     */
    public int getQuarterOf(PlayerColor color) {
        return switch (color) {
            case RED -> 0;
            case YELLOW -> 1;
            case BLUE -> 2;
            case GREEN -> 3;
        };
    }

    /**
     * Returns the home tiles of the given color, where its pieces start.
     *
     * @param color color of a player
     * @return the home tiles of the color, ordered by id
     */
    public List<Tile> getHomeTiles(PlayerColor color) {
        int quarter = getQuarterOf(color);
        return tiles.stream()
                .filter(tile -> tile.getType() == TileType.HOME && tile.getQuarter() == quarter)
                .toList();
    }

    /**
     * Returns the tile with the given id.
     *
     * @param tileId id of the tile
     * @return the tile
     * @throws IllegalArgumentException if no tile has this id
     */
    public Tile getTileById(int tileId) {
        return tiles.stream()
                .filter(tile -> tile.getId() == tileId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No tile with id " + tileId));
    }

    /**
     * Returns all tiles of the given quarter.
     *
     * @param quarter quarter of the tiles
     * @return list of all tiles of the quarter
     */
    public List<Tile> getTilesOfQuarter(int quarter) {
        return tiles.stream()
                .filter(tile -> tile.getQuarter() == quarter)
                .toList();
    }

    /**
     * Returns all tiles of the board.
     *
     * @return list of all tiles
     */
    public List<Tile> getTiles() {
        return List.copyOf(tiles);
    }

    /**
     * Checks that no two tiles of this board have the same id.
     *
     * @throws IllegalArgumentException if an id is used more than once
     */
    private void requireUniqueIds() {
        Set<Integer> ids = new HashSet<>();
        for (Tile tile : tiles) {
            if (!ids.add(tile.getId())) {
                throw new IllegalArgumentException("Duplicate tile id: " + tile.getId());
            }
        }
    }

    /**
     * Adds the track tiles of one quarter, in moving direction.
     * Every 5th tile is a safe space.
     *
     * @param quarter quarter to add the tiles for, from 0 to 3
     */
    private void addTrackTiles(int quarter) {
        for (int i = 1; i <= TILES_PER_QUARTER; i++) {
            int id = quarter * TILES_PER_QUARTER + i;
            TileType type = id % SAFE_INTERVAL == 0 ? TileType.SAFE : TileType.TRACK;
            tiles.add(new Tile(id, type, quarter, null));
        }
    }

    /**
     * Adds the home slots and the staircase of the color that belongs to the given quarter.
     *
     * @param quarter quarter of the color, from 0 to 3
     */
    private void addHomeAndStaircaseTiles(int quarter) {
        int firstId = (quarter + 1) * COLOR_ID_BLOCK;
        for (int i = 1; i <= HOME_SLOT_COUNT; i++) {
            tiles.add(new Tile(firstId + i, TileType.HOME, quarter, null));
        }
        for (int i = 1; i <= STAIRCASE_LENGTH; i++) {
            tiles.add(new Tile(firstId + HOME_SLOT_COUNT + i, TileType.STAIRCASE, quarter, null));
        }

    }
}
