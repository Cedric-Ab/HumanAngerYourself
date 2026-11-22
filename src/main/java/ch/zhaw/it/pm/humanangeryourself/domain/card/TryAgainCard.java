package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GamblingOutcome;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

public class TryAgainCard implements HandCard {
    @Override
    public void apply(Game game, Player player, Action action) {
        CardType cardType = game.getGamblingRules().spinCard().getCardType();
        player.addCard(cardType);

        GamblingOutcome gamblingOutcome = new GamblingOutcome(true, "You won " + cardType);
        game.getTurn().setGamblingOutcome(gamblingOutcome);
    }
}
