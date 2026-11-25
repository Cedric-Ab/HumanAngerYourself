package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import java.util.List;

/**
 * Interface for cards that require a target to be played.
 */
public interface Targeted<T extends Action> extends Card {
    /**
     * Determines whether the card requires further targeting based on the current game state and player.
     *
     * @param game   the current game instance
     * @param player the player who is playing the card
     * @return true if the card requires further targeting, false otherwise
     */
    default boolean requiresFurtherTarget(Game game, Player player) {
        return getPossibleTargets(game, player).isEmpty();
    }

    /**
     * Returns a list of possible targets for the card based on the current game state and player.
     *
     * @param game   the current game instance
     * @param player the player who is playing the card
     * @return a list of possible targets for the card
     */
    List<T> getPossibleTargets(Game game, Player player);

    /**
     * Sets a target for the card.
     *
     * @param target the target to set for the card
     */
    void setTarget(Action target);
}
