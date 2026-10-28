package ch.zhaw.it.pm.humanangeryourself.domain.model;

import ch.zhaw.it.pm.humanangeryourself.api.snapshot.CardOffer;

import java.util.List;

public record Npc(String name) {

    public List<CardOffer> getOffers() {
        // TODO: implement
        return List.of();
    }
}
