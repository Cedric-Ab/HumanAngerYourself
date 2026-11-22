package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.RollOutcome;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

public class SpeedBoostCard implements HandCard {

    @Override
    public void apply(Game game, Player player, Action action) {
        int newRoll = game.getMovementRules().rollDie() * 2;
        game.getTurn().setRollOutcome(new RollOutcome(newRoll));
    }
}
