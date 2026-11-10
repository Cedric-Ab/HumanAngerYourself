package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

/**
 * Game logic for action cards: checking whether a card may be played and applying its effect.
 */
public class CardEffects {

    /**
     * Checks whether the player is allowed to play the card described by the given action.
     *
     * @param player the player who wants to play the card
     * @param action the action describing the card play
     * @return {@code true} if the card can be played
     */
    public boolean canPlay(Player player, Action action) {
        return false;
    }

    /**
     * Applies the effect of a card. Not implemented yet.
     */
    public void apply() { }

    /**
     * Applies the effect of an instant card, which takes effect immediately when it is drawn
     * Not implemented yet.
     */
    public void applyInstant() { }

    /**
     * Applies the effect of a hand card, which the player keeps and plays at a later time.
     * Not implemented yet.
     */
    public void applyHandCard() { }
}
