package ch.zhaw.it.pm.humanangeryourself.api.action;

import ch.zhaw.it.pm.humanangeryourself.api.snapshot.CardOffer;

/**
 * Represent the action where a player buys a card from an NPC.
 *
 * @param offer the accepted offer
 */
public record BuyCard(
        CardOffer offer
) implements Action { }
