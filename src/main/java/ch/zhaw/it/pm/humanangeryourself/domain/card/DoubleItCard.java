package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.InvalidActionException;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import java.util.List;

/**
 * Hand card that lets an indebted player pass their debt, doubled, on to the
 * next player in turn order.
 */
public class DoubleItCard implements HandCard {

    /**
     * Doubles the player's debt, adds it to the balance of the next player in turn
     * order (wrapping around at the end of the player list), and resets the
     * playing player's balance to zero.
     *
     * @param game   the game in which the card is played
     * @param player the player who plays the card; must be in debt
     * @param action the action that triggered the card play
     * @throws InvalidActionException if the player's balance is positive, i.e. the
     *                                player is not in debt
     */
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

    @Override
    public boolean preconditionsMet() {
        //TODO
        return false;
    }

    @Override
    public CardType type() {
        return CardType.DOUBLE_IT;
    }
}
