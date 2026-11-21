package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GamblingOutcome;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Turn;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.GamblingRules;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

public class MoneyTigerCard implements HandCard{
    Turn turn;
    GamblingRules gamblingRules;
    @Override
    public void apply(Game game, Player player, Action action) {
        String result = String.valueOf(gamblingRules.spin() * 2);
        GamblingOutcome gamblingOutcome = new GamblingOutcome(false, String.format(result));
        turn.setGamblingOutcome(gamblingOutcome);
    }
}
