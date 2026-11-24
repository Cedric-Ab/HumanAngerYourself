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
    private final Npc npc;
    private Property property;

    /**
     * Creates a new tile.
     *
     * @param id      unique id of the tile on the board
     * @param type    type of the tile
     * @param quarter board quarter the tile belongs to, -1 if it belongs to no quarter
     * @param npc     NPC on this tile, or {@code null} if there is none
     * @throws NullPointerException     if {@code type} is {@code null}
     * @throws IllegalArgumentException if a property is placed on a tile that is not part of the track
     */
    public Tile(int id, TileType type, int quarter, Npc npc) {
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.id = id;
        this.quarter = quarter;
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
     * Sets the property on this tile.
     *
     * @param property the new property
     * @throws IllegalStateException if the tile already has a property
     * @throws NullPointerException  if the given property is null
     */
    public void setProperty(Property property) {
        if (this.property != null) {
            throw new IllegalStateException("Tile already has a property");
        }
        if (property == null) {
            throw new NullPointerException("Property must not be null");
        }
        this.property = property;
    }

    /**
     * Removes the property from this tile.
     *
     * @throws IllegalStateException if the tile does not have a property
     */
    public void removeProperty() {
        if (property == null) {
            throw new IllegalStateException("Tile does not have a property");
        }
        property = null;
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
