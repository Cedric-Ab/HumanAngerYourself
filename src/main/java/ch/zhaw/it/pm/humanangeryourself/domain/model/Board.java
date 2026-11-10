package ch.zhaw.it.pm.humanangeryourself.domain.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * The game board, made up of all tiles.
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
