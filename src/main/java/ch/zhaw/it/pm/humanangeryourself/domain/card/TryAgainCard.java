package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GamblingOutcome;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

/**
 * Hand card that performs a gambling spin for a new card instead of money.
 */
public class TryAgainCard implements HandCard {

    /**
     * Spins for a card, adds the won card
     * to the player's hand and records the result as the turn's gambling outcome.
     *
     * @param game   the game in which the card is played
     * @param player the player who receives the won card
     * @param action the action that triggered the card play
     */
    @Override
    public void apply(Game game, Player player, Action action) {
        CardType cardType = game.getGamblingRules().spinCard().getCardType();
        player.addCard(cardType);

        GamblingOutcome gamblingOutcome = new GamblingOutcome(true, "You won " + cardType);
        game.getTurn().setGamblingOutcome(gamblingOutcome);
    }
}
