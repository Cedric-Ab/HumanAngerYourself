package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult.GambleResult.CardResult;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

public class TryAgainCard implements HandCard {
    @Override
    public void apply(Game game, Player player, Action action) {
        // TODO: add actual card type
        game.getTurn().setPrimaryActionResult(new CardResult(null));
    }
}
