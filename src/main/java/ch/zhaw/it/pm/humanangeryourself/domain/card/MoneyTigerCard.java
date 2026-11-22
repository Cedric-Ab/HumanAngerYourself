package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult.GambleResult.MoneyResult;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

public class MoneyTigerCard implements HandCard {
    @Override
    public void apply(Game game, Player player, Action action) {
        // TODO: force spin() to return a monetary amount instead of a card
        long amount = ((MoneyResult) game.getGamblingRules().spin()).amount();
        game.getTurn().setPrimaryActionResult(new MoneyResult(amount * 2));
    }
}
