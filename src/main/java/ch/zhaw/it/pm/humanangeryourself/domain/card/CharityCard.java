package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

/**
 * Instant card that deducts a fixed amount of money from the player who drew it.
 */
public class CharityCard implements InstantCard{

    /**
     * Deducts 150,000 from the player's balance.
     *
     * @param game   the game in which the card is drawn
     * @param player the player whose balance is reduced
     */
    @Override
    public void apply(Game game, Player player) {
        player.changeBalanceBy(-150000);
    }
}
