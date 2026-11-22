package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

/**
 * Hand card that switches something between players.
 *
 * <p><strong>Note:</strong> not yet implemented.</p>
 */
public class SwitchCard implements HandCard{

    /**
     * Applies the card's effect. Currently a placeholder that does nothing.
     *
     * @param game   the game in which the card is played
     * @param player the player who plays the card
     * @param action the action that triggered the card play
     */
    @Override
    public void apply(Game game, Player player, Action action) {
        //TODO implement
    }
}
