package ch.zhaw.it.pm.humanangeryourself.domain.model;

import java.util.ArrayList;
import java.util.List;

public record Board(List<Tile> tiles) {
    public Board(List<Tile> tiles) {
        this.tiles = new ArrayList<>(tiles);
    }

    public boolean isBuyable(Tile tile) {
        // TODO
        return false;
    }

    public List<Property> getPropertiesOfPlayer(Player player) {
        // TODO
        return List.of();
    }

    public Tile getTileById(int tileId) {
        // TODO
        return null;
    }

    @Override
    public List<Tile> tiles() {
        return List.copyOf(tiles);
    }
}
