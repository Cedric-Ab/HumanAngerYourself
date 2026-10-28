package ch.zhaw.it.pm.humanangeryourself.domain.model;

import ch.zhaw.it.pm.humanangeryourself.api.snapshot.ShopOffer;

import java.util.List;

public class Npc {
    private final String name;

    public Npc(String name) {
        this.name = name;
    }

    public List<ShopOffer> getOffers() {
        // TODO: implement
        return List.of();
    }

    public String getName() {
        return name;
    }
}
