package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult.GambleResult.CardResult;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

/**
 * Hand card that performs a gambling spin for a new card instead of money.
 */
public class TryAgainCard implements HandCard {

    /**
     * Spins for a card, adds the won card
     * to the player's hand and records the result as the turn's primary action result.
     *
     * @param game   the game in which the card is played
     * @param player the player who receives the won card
     */
    @Override
    public void apply(Game game, Player player) {
        CardType cardType = game.getGamblingRules().spinCard().cardType();
        player.addCard(Game.cardFromType(cardType));

        game.getTurn().setPrimaryActionResult(new CardResult(cardType));
    }

    @Override
    public boolean preconditionsMet() {
        return true;
    }

    @Override
    public CardType type() {
        return CardType.TRY_AGAIN;
    }
}
