package ch.zhaw.it.pm.humanangeryourself.domain.card.types;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.action.InvalidActionException;
import ch.zhaw.it.pm.humanangeryourself.domain.card.ApplicableCard;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import java.util.List;
import java.util.Optional;

/**
 * Hand card that lets an indebted player pass their debt, doubled, on to the
 * next player in turn order.
 */
public class DoubleItCard implements ApplicableCard {

    /**
     * Doubles the player's debt, adds it to the balance of the next player in turn
     * order (wrapping around at the end of the player list), and resets the
     * playing player's balance to zero.
     *
     * @param game   the game in which the card is played
     * @param player the player who plays the card; must be in debt
     * @throws InvalidActionException if the player's balance is positive, i.e. the
     *                                player is not in debt
     */
    @Override
    public void apply(Game game, Player player) {
        if (!player.isInDebt()) {
            throw new InvalidActionException(player, "You must be in debt to play this card");
        }

        long debtDoubled = player.getMoney() * 2;
        nextPlayer(game, player).changeBalanceBy(debtDoubled);
        player.resetBalanceToZero();
    }

    @Override
    public CardType type() {
        return CardType.DOUBLE_IT;
    }

    /**
     * Returns the player who receives the debt and may therefore counter the card.
     *
     * @param game   the game in which the card is played
     * @param player the player who plays the card
     * @return the next player in turn order
     */
    @Override
    public Optional<Player> getDefender(Game game, Player player) {
        return Optional.of(nextPlayer(game, player));
    }

    /**
     * Returns the player after the given one in turn order, wrapping around at the end of the list.
     *
     * @param game   the game the players belong to
     * @param player the player whose successor is looked up
     * @return the next player in turn order
     */
    private static Player nextPlayer(Game game, Player player) {
        List<Player> players = game.getPlayers();
        int nextPlayerIndex = (players.indexOf(player) + 1) % players.size();
        return players.get(nextPlayerIndex);
    }

}
