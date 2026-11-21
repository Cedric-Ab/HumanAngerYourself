package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GamblingOutcome;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Turn;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

public class TryAgainCard implements HandCard{
    Turn turn;
    @Override
    public void apply(Game game, Player player, Action action) {
        GamblingOutcome gamblingOutcome = new GamblingOutcome(true, "");
        turn.setGamblingOutcome(gamblingOutcome);
    }
}
