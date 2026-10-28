package ch.zhaw.it.pm.humanangeryourself.api.snapshot;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;

/**
 * One of the offers made by an NPC.
 *
 * @param cardType type of the offered card
 * @param price price of the offered card
 */
public record CardOffer(
        CardType cardType,
        long price
) { }
