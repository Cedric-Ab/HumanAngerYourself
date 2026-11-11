package ch.zhaw.it.pm.humanangeryourself.domain.game;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.Economy;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.GamblingRules;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.MovementRules;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import java.util.List;

/**
 * Builder class for creating a Game instance with customizable rules, board, and players.
 */
public class GameBuilder {
    private MovementRules movementRules = new MovementRules();
    private GamblingRules gamblingRules = new GamblingRules();
    private Economy economy = new Economy();
    private Board board = new Board();
    private List<Player> players;

    /**
     * Creates a new GameBuilder instance with default rules, board, and player names.
     */
    public GameBuilder() {
    }

    private static List<Player> createPlayers(Board board, List<String> playerNames) {
        if (playerNames.size() != 4) {
            throw new IllegalArgumentException("Exactly 4 player names must be provided.");
        }

        return List.of(
                createPlayer(playerNames.get(0), PlayerColor.RED, board, 101),
                createPlayer(playerNames.get(1), PlayerColor.GREEN, board, 401),
                createPlayer(playerNames.get(2), PlayerColor.BLUE, board, 301),
                createPlayer(playerNames.get(3), PlayerColor.YELLOW, board, 201));
    }

    private static Player createPlayer(String name, PlayerColor color, Board board, int firstHomeTileId) {
        List<Piece> pieces = List.of(
                new Piece(board.getTileById(firstHomeTileId)),
                new Piece(board.getTileById(firstHomeTileId + 1)),
                new Piece(board.getTileById(firstHomeTileId + 2)),
                new Piece(board.getTileById(firstHomeTileId + 3)));
        return new Player(name, color, 1000, pieces);
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
     * @param players the list of players to set
     * @return the current GameBuilder instance for method chaining
     */
    public GameBuilder setPlayers(List<Player> players) {
        this.players = players;
        return this;
    }

    /**
     * Sets the player names for the game being built.
     * <p>
     * This method will create players with the given names and assign them colors and starting positions on the board.
     *
     * @param playerNames the list of player names to set, must contain exactly 4 names
     * @return the current GameBuilder instance for method chaining
     */
    public GameBuilder setPlayerNames(List<String> playerNames) {
        this.players = createPlayers(board, playerNames);
        return this;
    }

    /**
     * Creates a new Game instance with the configured rules, board, and players.
     *
     * @return a new Game instance
     */
    public Game createGame() {
        if (players == null) {
            players = createPlayers(board, List.of("Alice", "Bob", "Charlie", "Dave"));
        }

        injectDependencies();

        return new Game(movementRules, gamblingRules, economy, board, players);
    }

    private void injectDependencies() {
        movementRules.inject(board);
    }
}
