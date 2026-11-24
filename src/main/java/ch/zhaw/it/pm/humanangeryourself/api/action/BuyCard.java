package ch.zhaw.it.pm.humanangeryourself.api.action;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;

/**
 * Represent the action where a player buys a card from an NPC.
 * <p>
 * The price is not part of the action, it depends on the NPC. The UI finds it with
 * {@code snapshot.npcAt(tile).flatMap(npc -> npc.offerFor(card))}.
 * </p>
 *
 * @param tile id of the tile the selling NPC stands on
 * @param card type of the card to be bought
 */
public record BuyCard(
        int tile,
        CardType card
) implements Action { }
