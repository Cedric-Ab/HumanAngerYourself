package ch.zhaw.it.pm.humanangeryourself.domain.game;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerSetup;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult.RollResult;
import ch.zhaw.it.pm.humanangeryourself.config.GameConfig;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link Game}.
 */
class GameTest {

    private Game game;

    /**
     * Creates a game with one player for each of the given colors.
     */
    private static Game gameWith(PlayerColor... colors) {
        List<PlayerSetup> setups = Stream.of(colors)
                .map(color -> new PlayerSetup(color.name(), color))
                .toList();
        return GameFactory.create(GameConfig.defaults(), setups);
    }

    private PlayerColor currentColor() {
        return game.getTurn().getPlayer().getColor();
    }

    @BeforeEach
    void setUp() {
        game = gameWith(PlayerColor.BLUE, PlayerColor.RED, PlayerColor.YELLOW);
    }

    /**
     * Players take turns in the order of the board, whatever order they were created in.
     */
    @Test
    void turnsFollowBoardOrder() {
        assertEquals(PlayerColor.RED, currentColor());

        game.nextTurn();
        assertEquals(PlayerColor.YELLOW, currentColor());

        game.nextTurn();
        assertEquals(PlayerColor.BLUE, currentColor());

        game.nextTurn();
        assertEquals(PlayerColor.RED, currentColor());
    }

    /**
     * The result of the last player is gone when the next turn starts.
     */
    @Test
    void newTurnHasNoResult() {
        game.getTurn().setPrimaryActionResult(new RollResult(3));

        game.nextTurn();

        assertNull(game.getTurn().getPrimaryActionResult());
    }

    /**
     * Buying a card from an NPC only counts for the current turn.
     */
    @Test
    void newTurnAllowsNpcCard() {
        assertFalse(game.getTurn().hasBoughtNpcCard());

        game.getTurn().markNpcCardBought();
        assertTrue(game.getTurn().hasBoughtNpcCard());

        game.nextTurn();
        assertFalse(game.getTurn().hasBoughtNpcCard());
    }

    /**
     * Players start with the configured money, no cards and all pieces at home.
     */
    @Test
    void playersStartAtHome() {
        Player red = game.getPlayer(PlayerColor.RED);

        assertEquals(GameConfig.defaults().startMoney(), red.getMoney());
        assertTrue(red.getCards().isEmpty());
        assertEquals(game.getBoard().getHomeTiles(PlayerColor.RED),
                red.getPieces().stream().map(Piece::getCurrentTile).toList());
    }

    /**
     * Nobody has won a new game.
     */
    @Test
    void startsWithoutWinner() {
        assertTrue(game.getWinner().isEmpty());
    }

    /**
     * The game remembers who has won.
     */
    @Test
    void remembersWinner() {
        Player red = game.getPlayer(PlayerColor.RED);

        game.setAsWinner(red);

        assertSame(red, game.getWinner().orElseThrow());
    }

    /**
     * After someone has won, there are no more turns.
     */
    @Test
    void noTurnAfterWin() {
        game.setAsWinner(game.getPlayer(PlayerColor.RED));

        assertThrows(IllegalStateException.class, game::nextTurn);
    }

    /**
     * Someone who doesn't play in this game can't win it.
     */
    @Test
    void foreignPlayerCannotWin() {
        Player stranger = new Player("Stranger", PlayerColor.RED, 0, List.of());

        assertThrows(IllegalArgumentException.class, () -> game.setAsWinner(stranger));
        assertTrue(game.getWinner().isEmpty());
    }

    /**
     * Asking for a color nobody plays fails.
     */
    @Test
    void unusedColorFails() {
        assertThrows(IllegalArgumentException.class, () -> game.getPlayer(PlayerColor.GREEN));
    }

    /**
     * Two and four players are the limits.
     */
    @Test
    void acceptsTwoToFourPlayers() {
        assertEquals(2, gameWith(PlayerColor.RED, PlayerColor.BLUE).getPlayers().size());
        assertEquals(4, gameWith(PlayerColor.values()).getPlayers().size());
    }

    /**
     * Nobody can play alone.
     */
    @Test
    void rejectsTooFewPlayers() {
        assertThrows(IllegalArgumentException.class, () -> gameWith());
        assertThrows(IllegalArgumentException.class, () -> gameWith(PlayerColor.RED));
    }

    /**
     * Two players can't have the same color.
     */
    @Test
    void rejectsDuplicateColors() {
        assertThrows(IllegalArgumentException.class, () -> gameWith(PlayerColor.RED, PlayerColor.RED));
    }
}
