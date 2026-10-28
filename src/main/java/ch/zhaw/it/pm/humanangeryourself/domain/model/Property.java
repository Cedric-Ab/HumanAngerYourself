package ch.zhaw.it.pm.humanangeryourself.domain.model;

import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;
import ch.zhaw.it.pm.humanangeryourself.config.GameConfig;

import java.util.Optional;

public class Property {
    private final PropertySize size;
    private Player owner;

    public Property(PropertySize size) {
        this.size = size;
    }

    public long getPrice(GameConfig config) {
        // TODO
        return 0;
    }

    public long getRent(GameConfig config) {
        // TODO
        return 0;
    }

    public PropertySize getSize() {
        return size;
    }

    public Optional<Player> getOwner() {
        return Optional.ofNullable(owner);
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
