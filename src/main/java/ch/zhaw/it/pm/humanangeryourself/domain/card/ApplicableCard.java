package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

public interface ApplicableCard extends Card {

    /**
     * Applies the effect of this card.
     *
     * @param game   the game on which the card is applied
     * @param player the player on which the card is applied
     */
    void apply(Game game, Player player);

}
