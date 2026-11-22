package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

public class JackpotCard implements InstantCard {

    @Override
    public void apply(Game game, Player player) {
        player.changeBalanceBy(250000);
    }
}




