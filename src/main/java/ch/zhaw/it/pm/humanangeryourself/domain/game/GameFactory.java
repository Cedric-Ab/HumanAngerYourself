package ch.zhaw.it.pm.humanangeryourself.domain.game;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerSetup;
import ch.zhaw.it.pm.humanangeryourself.config.GameConfig;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.Economy;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.GamblingRules;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.MovementRules;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import java.util.Comparator;
import java.util.List;

/**
 * Creates a {@link Game} from the players entered on the start screen.
 * Keeps the setup logic out of {@link Game}, which only manages the game state.
 */
public final class GameFactory {

    /**
     * Utility class, no instances.
     */
    private GameFactory() { }

    /**
     * Creates a new game. The players are put into turn order by the position of their color on the board,
     * each player gets the start money and four pieces on their home tiles.
     *
     * @param config       settings for start money, rules and limits
     * @param playerSetups name and color of every player, in any order
     * @return a new game, ready for the first turn
     * @throws NullPointerException     if there are not 2 to 4 players or a color is used more than once
     * @throws IllegalArgumentException if the players are not valid for a game
     * @see Game#Game(GameConfig, MovementRules, GamblingRules, Economy, Board, List)
     */
    public static Game create(GameConfig config, List<PlayerSetup> playerSetups) {
        Board board = new Board();
        List<Player> players = playerSetups.stream()
                .sorted(Comparator.comparingInt(setup -> board.getQuarterOf(setup.color())))
                .map(setup -> createPlayer(setup, board, config.startMoney()))
                .toList();
        return new Game(config, new MovementRules(config, board), new GamblingRules(config.gambling()),
                new Economy(config), board, players);
    }

    /**
     * Creates a new game with the given configuration, board, players and rules.
     *
     * @param config        settings for start money, rules and limits
     * @param board         the board to use for the game
     * @param playerSetups  name and color of every player, in any order
     * @param movementRules movement rules to use for the game
     * @param gamblingRules gambling rules to use for the game
     * @return a new game, ready for the first turn
     * @throws NullPointerException     if there are not 2 to 4 players or
     *                                  a color is used more than once or any argument is null
     * @throws IllegalArgumentException if the players are not valid for a game
     * @see Game#Game(GameConfig, MovementRules, GamblingRules, Economy, Board, List)
     */
    public static Game create(GameConfig config, Board board, List<PlayerSetup> playerSetups,
                              MovementRules movementRules, GamblingRules gamblingRules) {
        List<Player> players = playerSetups.stream()
                .sorted(Comparator.comparingInt(setup -> board.getQuarterOf(setup.color())))
                .map(setup -> createPlayer(setup, board, config.startMoney()))
                .toList();
        return new Game(config, movementRules, gamblingRules, new Economy(config), board, players);
    }

    /**
     * Creates a player with the start money and one piece on each home tile of the player's color.
     * Each piece gets the id of its home tile, so every piece on the board has its own id.
     *
     * @param setup      name and color of the player
     * @param board      board that provides the home tiles
     * @param startMoney money the player starts with
     * @return the new player
     */
    private static Player createPlayer(PlayerSetup setup, Board board, long startMoney) {
        List<Piece> pieces = board.getHomeTiles(setup.color()).stream()
                .map(homeTile -> new Piece(homeTile.getId(), homeTile))
                .toList();
        return new Player(setup.name(), setup.color(), startMoney, pieces);
    }
}
