package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerSetup;
import ch.zhaw.it.pm.humanangeryourself.api.action.PlayCard;
import ch.zhaw.it.pm.humanangeryourself.config.GameConfig;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.game.GameFactory;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.GamblingRules;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.MovementRules;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Sets up a game with three players and controllable randomness for the card tests.
 */
abstract class CardTestBase {

    protected GamblingRules gamblingRules;
    protected MovementRules movementRules;
    protected Game game;
    protected Player red;
    protected Player yellow;
    protected Player blue;

    @BeforeEach
    void setUpGame() {
        gamblingRules = mock(GamblingRules.class);
        movementRules = mock(MovementRules.class);
        game = GameFactory.create(GameConfig.defaults(), new Board(), List.of(
                new PlayerSetup("Red", PlayerColor.RED),
                new PlayerSetup("Yellow", PlayerColor.YELLOW),
                new PlayerSetup("Blue", PlayerColor.BLUE)), movementRules, gamblingRules);
        red = game.getPlayer(PlayerColor.RED);
        yellow = game.getPlayer(PlayerColor.YELLOW);
        blue = game.getPlayer(PlayerColor.BLUE);
    }

    /**
     * Gives the player exactly the given amount of money.
     */
    protected static void setMoney(Player player, long money) {
        player.resetBalanceToZero();
        player.changeBalanceBy(money);
    }

    /**
     * Lets the player play the given card.
     */
    protected void play(HandCard card, Player player) {
        card.apply(game, player, new PlayCard(card.type()));
    }
}
