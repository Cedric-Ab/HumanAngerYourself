package ch.zhaw.it.pm.humanangeryourself.domain.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A single field on the board.
 * A tile can carry a {@link Property} (which can be bought) and/or an {@link Npc} (which sells cards).
 * <p>
 * Tile ids (same numbers as the fx:ids "tile&lt;id&gt;" in BoardView.fxml):
 * </p>
 * <ul>
 *     <li>{@code 1 - 60}: main track, counter-clockwise, every 5th tile is a safe space</li>
 *     <li>{@code 100}: shared finish in the centre</li>
 *     <li>{@code X01 - X04}: home slots of colour X</li>
 *     <li>{@code X05 - X14}: staircase of colour X, X05 is next to the track, X14 next to the finish</li>
 * </ul>
 * <p>
 * Colour X: 1 = red, 2 = yellow, 3 = blue, 4 = green (counter-clockwise, X - 1 is also the quarter).
 * </p>
 */
public class Tile {
    private final int id;
    private final TileType type;
    private final int quarter;
    private final Property property;
    private final Npc npc;

    /**
     * Creates a new tile.
     *
     * @param id       unique id of the tile on the board
     * @param type     type of the tile
     * @param quarter  board quarter the tile belongs to, -1 if it belongs to no quarter
     * @param property property on this tile, or {@code null} if there is none
     * @param npc      NPC on this tile, or {@code null} if there is none
     * @throws NullPointerException     if {@code type} is {@code null}
     * @throws IllegalArgumentException if a property is placed on a tile that is not part of the track
     */
    public Tile(int id, TileType type, int quarter, Property property, Npc npc) {
        this.type = Objects.requireNonNull(type, "type must not be null");
        if (property != null && type != TileType.TRACK) {
            throw new IllegalArgumentException("Properties can only be placed on TRACK tiles");
        }
        this.id = id;
        this.quarter = quarter;
        this.property = property;
        this.npc = npc;
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
     * Returns whether a piece on this tile is safe from being sent home.
     * Only normal track tiles are unsafe, every other tile type protects the piece.
     *
     * @return {@code true} if pieces on this tile cannot be sent home
     */
    public boolean isSafe() {
        return type != TileType.TRACK;
    }

    /**
     * Returns whether this tile has a property that nobody owns yet.
     *
     * @return {@code true} if the property on this tile can be bought
     */
    public boolean isBuyable() {
        return property != null && property.getOwner().isEmpty();
    }

    /**
     * Returns the unique id of the tile.
     *
     * @return the tile id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the type of the tile.
     *
     * @return the tile type, never {@code null}
     */
    public TileType getType() {
        return type;
    }

    /**
     * Returns the board quarter the tile belongs to.
     *
     * @return the quarter (0 to 3), or -1 if the tile belongs to no quarter
     */
    public int getQuarter() {
        return quarter;
    }

    /**
     * Returns the property on this tile.
     *
     * @return the property, or an empty Optional if there is none
     */
    public Optional<Property> getProperty() {
        return Optional.ofNullable(property);
    }

    /**
     * Returns the NPC on this tile.
     *
     * @return the NPC, or an empty Optional if there is none
     */
    public Optional<Npc> getNpc() {
        return Optional.ofNullable(npc);
    }
}
