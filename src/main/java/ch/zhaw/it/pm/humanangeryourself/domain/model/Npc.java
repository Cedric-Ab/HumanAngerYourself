package ch.zhaw.it.pm.humanangeryourself.domain.model;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.CardOffer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A non-player character on the board that sells action cards.
 */
public class Npc {
    /**
     * Name of the NPC.
     */
    private final String name;

    /**
     * Cards this NPC sells, with their prices.
     */
    private final List<CardOffer> offers;

    /**
     * Creates a new NPC with the given offers.
     *
     * @param name   name of the NPC
     * @param offers cards this NPC sells and their prices
     * @throws NullPointerException if an argument is {@code null}
     */
    public Npc(String name, List<CardOffer> offers) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.offers = List.copyOf(Objects.requireNonNull(offers, "offers must not be null"));
    }

    /**
     * Returns the cards this NPC sells.
     *
     * @return unmodifiable list of offers
     */
    public List<CardOffer> getOffers() {
        return offers;
    }

    /**
     * Returns the offer of this NPC for the given card type.
     *
     * @param cardType type of the card to look for
     * @return the offer, or an empty Optional if this NPC does not sell the card
     */
    public Optional<CardOffer> findOffer(CardType cardType) {
        return offers.stream()
                .filter(offer -> offer.cardType() == cardType)
                .findFirst();
    }

    /**
     * Returns the name of the NPC.
     *
     * @return the NPC's name, never {@code null}
     */
    public String getName() {
        return name;
    }
}
