package ch.zhaw.it.pm.humanangeryourself.domain.model;

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
