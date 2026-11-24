package ch.zhaw.it.pm.humanangeryourself.api.snapshot;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;

import java.util.List;
import java.util.Optional;

/**
 * Information about an NPC.
 * <p>
 * Contains all offers of the NPC, also those the viewer cannot buy right now.
 * Which cards can be bought right now is given by the
 * {@link ch.zhaw.it.pm.humanangeryourself.api.action.BuyCard} actions in the {@link DecisionInfo}.
 * </p>
 *
 * @param tile   where the NPC is located
 * @param name   name of the NPC
 * @param offers cards and their prices offered by this NPC
 */
public record NpcInfo(
        int tile,
        String name,
        List<CardOffer> offers
) {
    /**
     * Returns the offer of this NPC for the given card type.
     *
     * @param cardType type of the card to look for
     * @return the offer with its price or an empty Optional if this NPC does not sell the card
     */
    public Optional<CardOffer> offerFor(CardType cardType) {
        return offers.stream()
                .filter(offer -> offer.cardType() == cardType)
                .findFirst();
    }
}
