package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

/**
 * Instant card that awards a fixed amount of money to the player who drew it.
 */
public class JackpotCard implements InstantCard {

    /**
     * Adds 250,000 to the player's balance.
     *
     * @param game   the game in which the card is drawn
     * @param player the player whose balance is increased
     */
    @Override
    public void apply(Game game, Player player) {
        player.changeBalanceBy(250000);
    }
}




