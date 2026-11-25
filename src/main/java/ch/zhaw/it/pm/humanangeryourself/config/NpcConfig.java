package ch.zhaw.it.pm.humanangeryourself.config;


import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.CardOffer;

import java.util.List;
import java.util.Objects;

/**
 * Settings for one NPC shop, position, which cards it sells and at which price.
 *
 * @param name   name of the NPC
 * @param tile   id of the safe tile the NPC stands on
 * @param offers cards the NPC sells with their prices
 */
public record NpcConfig(String name, int tile, List<CardOffer> offers) {


    /**
     * Validates the values.
     *
     * @throws NullPointerException     if {@code name} or {@code offers} is {@code null}
     * @throws IllegalArgumentException if there are no offers, a price is negative,
     *                                  an instant card is offered or a card type is offered twice
     */
    public NpcConfig {
        Objects.requireNonNull(name, "name must not be null");
        offers = List.copyOf(Objects.requireNonNull(offers, "offers must not be null"));
        if (offers.isEmpty()) {
            throw new IllegalArgumentException("NPC " + name + " must offer at least one card");
        }
        if (offers.stream().anyMatch(offer -> offer.price() < 0 || offer.cardType().isInstant())) {
            throw new IllegalArgumentException("NPC " + name + " may only sell hand cards with a price >= 0");
        }
        if (offers.stream().map(CardOffer::cardType).distinct().count() != offers.size()) {
            throw new IllegalArgumentException("NPC " + name + " offers a card type more than once");
        }
    }


    /**
     * Returns the NPC shops, one in the middle of every board quarter.
     *
     * @return the NPC shops
     */
    public static List<NpcConfig> defaults() {
        return List.of(
                new NpcConfig("Grumpy old Woman", 10, List.of(
                        new CardOffer(CardType.SPEED_BOOST, 20_000),
                        new CardOffer(CardType.SWITCH, 40_000))),
                new NpcConfig("Honest Salesman", 25, List.of(
                        new CardOffer(CardType.TRY_AGAIN, 30_000),
                        new CardOffer(CardType.MONEY_TIGER, 50_000))),
                new NpcConfig("Trader", 40, List.of(
                        new CardOffer(CardType.BLOCK, 35_000),
                        new CardOffer(CardType.DOUBLE_IT, 60_000))),
                new NpcConfig("Trickster", 55, List.of(
                        new CardOffer(CardType.SWITCH, 45_000),
                        new CardOffer(CardType.BLOCK, 40_000),
                        new CardOffer(CardType.SPEED_BOOST, 25_000))));
    }

}
