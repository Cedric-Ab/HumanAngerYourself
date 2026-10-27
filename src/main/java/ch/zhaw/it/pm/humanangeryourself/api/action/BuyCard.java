package ch.zhaw.it.pm.humanangeryourself.api.action;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;

/**
 * Represent the action where a player buys a card from an NPC.
 *
 * @param card type of the card to be bought
 */
public record BuyCard(
        CardType card
) implements Action { }
