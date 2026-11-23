package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult.RollResult;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

/**
 * Hand card that replaces the current die roll with a new roll that is doubled.
 */
public class SpeedBoostCard implements HandCard {

    /**
     * Rolls the die, doubles the result and sets it as the roll outcome
     * of the current turn.
     *
     * @param game   the game in which the card is played
     * @param player the player who plays the card
     * @param action the action that triggered the card play
     */
    @Override
    public void apply(Game game, Player player, Action action) {
        int newRoll = game.getMovementRules().rollDie() * 2;
        game.getTurn().setPrimaryActionResult(new RollResult(newRoll));
    }

    @Override
    public boolean preconditionsMet() {
        return true;
    }

    @Override
    public CardType type() {
        return CardType.SPEED_BOOST;
    }
}
