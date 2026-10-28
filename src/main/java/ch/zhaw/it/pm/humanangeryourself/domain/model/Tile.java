package ch.zhaw.it.pm.humanangeryourself.domain.model;

import java.util.Optional;

public class Tile {
    private final int id;
    private final TileType type;
    private final int quarter;
    private final Property property;
    private final Npc npc;

    // CHECKSTYLE.OFF: ParameterNumber
    public Tile(int id, TileType type, int quarter, Property property, Npc npc) {
        this.id = id;
        this.type = type;
        this.quarter = quarter;
        this.property = property;
        this.npc = npc;
    }
    // CHECKSTYLE.ON: ParameterNumber

    public boolean isSafe() {
        // TODO
        return false;
    }

    public boolean isBuyable() {
        // TODO
        return false;
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
