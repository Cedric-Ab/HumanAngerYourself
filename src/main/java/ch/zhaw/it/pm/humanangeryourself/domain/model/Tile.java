package ch.zhaw.it.pm.humanangeryourself.domain.model;

import java.util.Objects;
import java.util.Optional;

/**
 * A single field on the board.
 * A tile can carry a {@link Property} (which can be bought) and/or an {@link Npc} (which sells cards).
 **/
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
     * @param quarter  board quarter the tile belongs to
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

    public int getId() {
        return id;
    }

    public TileType getType() {
        return type;
    }

    public int getQuarter() {
        return quarter;
    }

    public Optional<Property> getProperty() {
        return Optional.ofNullable(property);
    }

    public Optional<Npc> getNpc() {
        return Optional.ofNullable(npc);
    }
}
