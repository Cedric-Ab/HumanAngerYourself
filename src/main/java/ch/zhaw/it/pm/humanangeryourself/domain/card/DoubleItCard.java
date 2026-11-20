package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.InvalidActionException;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import java.util.List;

public class DoubleItCard implements HandCard {

    @Override
    public void apply(Game game, Player player, Action action) {
        if (player.getMoney() > 0) {
            throw new InvalidActionException(action, "You must be in debt to play this card");
        }

        long debtDoubled = player.getMoney() * 2;
        List<Player> players = game.getPlayers();
        int currentPlayerIndex = players.indexOf(player);
        int nextPlayerIndex = (currentPlayerIndex + 1) % players.size();
        players.get(nextPlayerIndex).changeBalanceBy(debtDoubled);
        player.resetBalanceToZero();
    }
}
