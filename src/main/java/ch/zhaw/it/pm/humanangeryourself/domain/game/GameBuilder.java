package ch.zhaw.it.pm.humanangeryourself.domain.game;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
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
 * Builder class for creating a Game instance with customizable rules, board, and players.
 */
public class GameBuilder {
    private GameConfig gameConfig = GameConfig.defaults();
    private MovementRules movementRules = new MovementRules();
    private GamblingRules gamblingRules = new GamblingRules();
    private Economy economy = new Economy();
    private Board board = new Board();
    private List<Player> players;
    private List<PlayerSetup> playerSetups = List.of(
            new PlayerSetup("Alice", PlayerColor.RED),
            new PlayerSetup("Bob", PlayerColor.GREEN),
            new PlayerSetup("Charlie", PlayerColor.BLUE),
            new PlayerSetup("Dave", PlayerColor.YELLOW)
    );

    /**
     * Creates a new GameBuilder instance with default rules, board, and player names.
     */
    public GameBuilder() {
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

    /**
     * Sets the game configuration for the game being built.
     *
     * @param gameConfig the game configuration to set
     * @return the current GameBuilder instance for method chaining
     */
    public GameBuilder setGameConfig(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
        return this;
    }

    /**
     * Sets the movement rules for the game being built.
     *
     * @param movementRules the movement rules to set
     * @return the current GameBuilder instance for method chaining
     */
    public GameBuilder setMovementRules(MovementRules movementRules) {
        this.movementRules = movementRules;
        return this;
    }

    /**
     * Sets the gambling rules for the game being built.
     *
     * @param gamblingRules the gambling rules to set
     * @return the current GameBuilder instance for method chaining
     */
    public GameBuilder setGamblingRules(GamblingRules gamblingRules) {
        this.gamblingRules = gamblingRules;
        return this;
    }

    /**
     * Sets the economy for the game being built.
     *
     * @param economy the economy to set
     * @return the current GameBuilder instance for method chaining
     */
    public GameBuilder setEconomy(Economy economy) {
        this.economy = economy;
        return this;
    }

    /**
     * Sets the board for the game being built.
     *
     * @param board the board to set
     * @return the current GameBuilder instance for method chaining
     */
    public GameBuilder setBoard(Board board) {
        this.board = board;
        return this;
    }

    /**
     * Sets the players for the game being built.
     *
     * @param players the list of players to set,
     *                must contain {@value Game#MIN_PLAYERS} to {@value Game#MAX_PLAYERS} players
     * @return the current GameBuilder instance for method chaining
     */
    public GameBuilder setPlayers(List<Player> players) {
        this.players = players;
        return this;
    }

    /**
     * Sets the player setups for the game being built.
     *
     * @param playerSetups the list of player setups to set,
     *                     must contain {@value Game#MIN_PLAYERS} to {@value Game#MAX_PLAYERS} setups
     * @return the current GameBuilder instance for method chaining
     */
    public GameBuilder setPlayerSetups(List<PlayerSetup> playerSetups) {
        this.playerSetups = playerSetups;
        return this;
    }

    /**
     * Creates a new Game instance with the configured rules, board, and players.
     *
     * @return a new Game instance
     * @see Game#Game(MovementRules, GamblingRules, Economy, Board, List)
     */
    public Game createGame() {
        if (players == null) {
            players = createPlayersFromSetups();
        }

        injectDependencies();

        return new Game(movementRules, gamblingRules, economy, board, players);
    }

    /**
     * Creates a list of players from the provided player setups, sorted by their color's position on the board.
     *
     * @return a list of players created from the player setups
     */
    private List<Player> createPlayersFromSetups() {
        return playerSetups.stream()
                .sorted(Comparator.comparingInt(setup -> board.getQuarterOf(setup.color())))
                .map(setup -> createPlayer(setup, board, gameConfig.startMoney()))
                .toList();
    }

    private void injectDependencies() {
        movementRules.inject(board);
        movementRules.inject(gameConfig);

        gamblingRules.inject(gameConfig.gambling());

        economy.inject(gameConfig);
    }
}
