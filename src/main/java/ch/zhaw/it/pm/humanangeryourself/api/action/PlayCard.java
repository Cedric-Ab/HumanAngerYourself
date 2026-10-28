package ch.zhaw.it.pm.humanangeryourself.api.action;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;

/**
 * Represent the action where a player activates one of the cards in their hand.
 *
 * @param card type of the card to be activated
 */
public record PlayCard(
        CardType card
) implements Action { }
