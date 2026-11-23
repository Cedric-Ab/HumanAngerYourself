package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult.GambleResult.MoneyResult;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

/**
 * Hand card that performs a gambling spin and pays out double the winnings.
 */
public class MoneyTigerCard implements HandCard {

    /**
     * Spins the win amount according to the game's gambling rules, doubles it,
     * credits it to the player and records the result as the turn's primary action result.
     *
     * @param game   the game in which the card is played
     * @param player the player who receives the winnings
     * @param action the action that triggered the card play
     */
    @Override
    public void apply(Game game, Player player, Action action) {
        long result = game.getGamblingRules().spinWin().amount() * 2;
        player.changeBalanceBy(result);

        game.getTurn().setPrimaryActionResult(new MoneyResult(result));
    }
}
