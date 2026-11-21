package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.RollOutcome;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Turn;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.MovementRules;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

public class SpeedBoostCard implements HandCard{
    MovementRules movementRules;
    Turn turn;
    @Override
    public void apply(Game game, Player player, Action action) {
        int newRoll = movementRules.rollDie() * 2;
        turn.setRollOutcome(new RollOutcome(newRoll));
    }
}
