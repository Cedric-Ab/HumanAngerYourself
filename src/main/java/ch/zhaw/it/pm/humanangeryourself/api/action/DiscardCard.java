package ch.zhaw.it.pm.humanangeryourself.api.action;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;

/**
 * Represent the action where a player discards a card from their hand.
 *
 * @param card type of the card to be discarded
 */
public record DiscardCard(
        CardType card
) implements Action { }
