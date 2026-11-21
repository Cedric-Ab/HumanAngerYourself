package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GamblingOutcome;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

public class TryAgainCard implements HandCard {
    @Override
    public void apply(Game game, Player player, Action action) {
        GamblingOutcome gamblingOutcome = new GamblingOutcome(true, "");
        game.getTurn().setGamblingOutcome(gamblingOutcome);
    }
}
