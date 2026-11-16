package ch.zhaw.it.pm.humanangeryourself.domain.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * The board class is responsible for creating and managing the tiles of the game board.
 * <p>
 * It knows all tiles and how they are connected.
 * It can answer questions about the tiles, such as whether a tile is buyable or which properties belong to a player.
 */
public class Board {
    private final List<Tile> tiles;

    /**
     * Creates a new board.
     *
     * @param tiles all tiles of the board
     * @throws NullPointerException     if {@code tiles} or one of its elements is {@code null}
     * @throws IllegalArgumentException if two tiles have the same id
     */
    public Board(List<Tile> tiles) {
        Objects.requireNonNull(tiles, "tiles must not be null");
        Set<Integer> ids = new HashSet<>();
        for (Tile tile : tiles) {
            Objects.requireNonNull(tile, "tiles must not contain null");
            if (!ids.add(tile.getId())) {
                throw new IllegalArgumentException("Duplicate tile id: " + tile.getId());
            }
        }
        this.tiles = new ArrayList<>(tiles);
    }

    /**
     * Creates a new board with all tiles, matching BoardView.fxml.
     */
    public Board() {
        this(createAll());
    }

    /**
     * Creates all tiles of the board, matching BoardView.fxml.
     *
     * @return unmodifiable list of all tiles, the track in moving direction
     */
    public static List<Tile> createAll() {
        return List.of(
                // Main track 1 - 60, counter-clockwise
                // Quarter 0 (red)
                new Tile(1, TileType.TRACK, 0, null, null),
                new Tile(2, TileType.TRACK, 0, null, null),
                new Tile(3, TileType.TRACK, 0, null, null),
                new Tile(4, TileType.TRACK, 0, null, null),
                new Tile(5, TileType.SAFE, 0, null, null),
                new Tile(6, TileType.TRACK, 0, null, null),
                new Tile(7, TileType.TRACK, 0, null, null),
                new Tile(8, TileType.TRACK, 0, null, null),
                new Tile(9, TileType.TRACK, 0, null, null),
                new Tile(10, TileType.SAFE, 0, null, null),
                new Tile(11, TileType.TRACK, 0, null, null),
                new Tile(12, TileType.TRACK, 0, null, null),
                new Tile(13, TileType.TRACK, 0, null, null),
                new Tile(14, TileType.TRACK, 0, null, null),
                new Tile(15, TileType.SAFE, 0, null, null),
                // Quarter 1 (yellow)
                new Tile(16, TileType.TRACK, 1, null, null),
                new Tile(17, TileType.TRACK, 1, null, null),
                new Tile(18, TileType.TRACK, 1, null, null),
                new Tile(19, TileType.TRACK, 1, null, null),
                new Tile(20, TileType.SAFE, 1, null, null),
                new Tile(21, TileType.TRACK, 1, null, null),
                new Tile(22, TileType.TRACK, 1, null, null),
                new Tile(23, TileType.TRACK, 1, null, null),
                new Tile(24, TileType.TRACK, 1, null, null),
                new Tile(25, TileType.SAFE, 1, null, null),
                new Tile(26, TileType.TRACK, 1, null, null),
                new Tile(27, TileType.TRACK, 1, null, null),
                new Tile(28, TileType.TRACK, 1, null, null),
                new Tile(29, TileType.TRACK, 1, null, null),
                new Tile(30, TileType.SAFE, 1, null, null),
                // Quarter 2 (blue)
                new Tile(31, TileType.TRACK, 2, null, null),
                new Tile(32, TileType.TRACK, 2, null, null),
                new Tile(33, TileType.TRACK, 2, null, null),
                new Tile(34, TileType.TRACK, 2, null, null),
                new Tile(35, TileType.SAFE, 2, null, null),
                new Tile(36, TileType.TRACK, 2, null, null),
                new Tile(37, TileType.TRACK, 2, null, null),
                new Tile(38, TileType.TRACK, 2, null, null),
                new Tile(39, TileType.TRACK, 2, null, null),
                new Tile(40, TileType.SAFE, 2, null, null),
                new Tile(41, TileType.TRACK, 2, null, null),
                new Tile(42, TileType.TRACK, 2, null, null),
                new Tile(43, TileType.TRACK, 2, null, null),
                new Tile(44, TileType.TRACK, 2, null, null),
                new Tile(45, TileType.SAFE, 2, null, null),
                // Quarter 3 (green)
                new Tile(46, TileType.TRACK, 3, null, null),
                new Tile(47, TileType.TRACK, 3, null, null),
                new Tile(48, TileType.TRACK, 3, null, null),
                new Tile(49, TileType.TRACK, 3, null, null),
                new Tile(50, TileType.SAFE, 3, null, null),
                new Tile(51, TileType.TRACK, 3, null, null),
                new Tile(52, TileType.TRACK, 3, null, null),
                new Tile(53, TileType.TRACK, 3, null, null),
                new Tile(54, TileType.TRACK, 3, null, null),
                new Tile(55, TileType.SAFE, 3, null, null),
                new Tile(56, TileType.TRACK, 3, null, null),
                new Tile(57, TileType.TRACK, 3, null, null),
                new Tile(58, TileType.TRACK, 3, null, null),
                new Tile(59, TileType.TRACK, 3, null, null),
                new Tile(60, TileType.SAFE, 3, null, null),

                // Shared finish in the centre
                new Tile(100, TileType.FINISH_AREA, -1, null, null),

                // Red: home slots, then staircase (from the track towards the finish)
                new Tile(101, TileType.HOME, 0, null, null),
                new Tile(102, TileType.HOME, 0, null, null),
                new Tile(103, TileType.HOME, 0, null, null),
                new Tile(104, TileType.HOME, 0, null, null),
                new Tile(105, TileType.STAIRCASE, 0, null, null),
                new Tile(106, TileType.STAIRCASE, 0, null, null),
                new Tile(107, TileType.STAIRCASE, 0, null, null),
                new Tile(108, TileType.STAIRCASE, 0, null, null),
                new Tile(109, TileType.STAIRCASE, 0, null, null),
                new Tile(110, TileType.STAIRCASE, 0, null, null),
                new Tile(111, TileType.STAIRCASE, 0, null, null),
                new Tile(112, TileType.STAIRCASE, 0, null, null),
                new Tile(113, TileType.STAIRCASE, 0, null, null),
                new Tile(114, TileType.STAIRCASE, 0, null, null),

                // Yellow: home slots, then staircase (from the track towards the finish)
                new Tile(201, TileType.HOME, 1, null, null),
                new Tile(202, TileType.HOME, 1, null, null),
                new Tile(203, TileType.HOME, 1, null, null),
                new Tile(204, TileType.HOME, 1, null, null),
                new Tile(205, TileType.STAIRCASE, 1, null, null),
                new Tile(206, TileType.STAIRCASE, 1, null, null),
                new Tile(207, TileType.STAIRCASE, 1, null, null),
                new Tile(208, TileType.STAIRCASE, 1, null, null),
                new Tile(209, TileType.STAIRCASE, 1, null, null),
                new Tile(210, TileType.STAIRCASE, 1, null, null),
                new Tile(211, TileType.STAIRCASE, 1, null, null),
                new Tile(212, TileType.STAIRCASE, 1, null, null),
                new Tile(213, TileType.STAIRCASE, 1, null, null),
                new Tile(214, TileType.STAIRCASE, 1, null, null),

                // Blue: home slots, then staircase (from the track towards the finish)
                new Tile(301, TileType.HOME, 2, null, null),
                new Tile(302, TileType.HOME, 2, null, null),
                new Tile(303, TileType.HOME, 2, null, null),
                new Tile(304, TileType.HOME, 2, null, null),
                new Tile(305, TileType.STAIRCASE, 2, null, null),
                new Tile(306, TileType.STAIRCASE, 2, null, null),
                new Tile(307, TileType.STAIRCASE, 2, null, null),
                new Tile(308, TileType.STAIRCASE, 2, null, null),
                new Tile(309, TileType.STAIRCASE, 2, null, null),
                new Tile(310, TileType.STAIRCASE, 2, null, null),
                new Tile(311, TileType.STAIRCASE, 2, null, null),
                new Tile(312, TileType.STAIRCASE, 2, null, null),
                new Tile(313, TileType.STAIRCASE, 2, null, null),
                new Tile(314, TileType.STAIRCASE, 2, null, null),

                // Green: home slots, then staircase (from the track towards the finish)
                new Tile(401, TileType.HOME, 3, null, null),
                new Tile(402, TileType.HOME, 3, null, null),
                new Tile(403, TileType.HOME, 3, null, null),
                new Tile(404, TileType.HOME, 3, null, null),
                new Tile(405, TileType.STAIRCASE, 3, null, null),
                new Tile(406, TileType.STAIRCASE, 3, null, null),
                new Tile(407, TileType.STAIRCASE, 3, null, null),
                new Tile(408, TileType.STAIRCASE, 3, null, null),
                new Tile(409, TileType.STAIRCASE, 3, null, null),
                new Tile(410, TileType.STAIRCASE, 3, null, null),
                new Tile(411, TileType.STAIRCASE, 3, null, null),
                new Tile(412, TileType.STAIRCASE, 3, null, null),
                new Tile(413, TileType.STAIRCASE, 3, null, null),
                new Tile(414, TileType.STAIRCASE, 3, null, null)
        );
    }

    /**
     * Returns whether the given tile is on this board and has a property nobody owns yet.
     *
     * @param tile tile to check
     * @return {@code true} if the property on the tile can be bought
     */
    public boolean isBuyable(Tile tile) {
        return tiles.contains(tile) && tile.isBuyable();
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
                .filter(property -> property.getOwner().filter(player::equals).isPresent())
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
     * Returns all tiles of the board.
     *
     * @return list of all tiles
     */
    public List<Tile> getTiles() {
        return List.copyOf(tiles);
    }
}
