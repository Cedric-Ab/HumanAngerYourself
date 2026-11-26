package ch.zhaw.it.pm.humanangeryourself.domain.card.types;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.domain.card.ApplicableCard;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

/**
 * Hand card that switches something between players.
 *
 * <p><strong>Note:</strong> not yet implemented.</p>
 */
public class SwitchCard implements ApplicableCard {

    /**
     * Applies the card's effect. Currently a placeholder that does nothing.
     *
     * @param game   the game in which the card is played
     * @param player the player who plays the card
     */
    @Override
    public void apply(Game game, Player player) {
        //TODO implement
    }

    @Override
    public CardType type() {
        return CardType.SWITCH;
    }
}
