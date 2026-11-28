package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import java.util.Optional;

public interface ApplicableCard extends Card {

    /**
     * Applies the effect of this card.
     *
     * @param game   the game on which the card is applied
     * @param player the player on which the card is applied
     */
    void apply(Game game, Player player);

    /**
     * Returns the player who is attacked by this card and may therefore counter it.
     *
     * @param game   the game in which the card is played
     * @param player the player who plays the card
     * @return the attacked player, or empty if the card attacks nobody
     */
    default Optional<Player> getDefender(Game game, Player player) {
        return Optional.empty();
    }

}
