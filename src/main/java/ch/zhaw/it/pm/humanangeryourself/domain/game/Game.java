package ch.zhaw.it.pm.humanangeryourself.domain.game;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.Economy;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.GamblingRules;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.MovementRules;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;

import java.util.List;
import java.util.Objects;

/**
 * Represents a game of Human Anger Yourself, containing the board, players, and the current turn.
 */
public class Game {
    private final MovementRules movementRules;
    private final GamblingRules gamblingRules;
    private final Economy economy;

    private final Board board;
    private final List<Player> players;

    /**
     * The current turn, which tells whose turn it is.
     */
    private Turn turn;

    /**
     * Creates a new game with default settings.
     */
    public Game() {
        this(createDefaultSetup());
    }

    private Game(DefaultSetup setup) {
        this(new MovementRules(setup.board()), new GamblingRules(), new Economy(),
                setup.board(), setup.players());
    }

    /**
     * Creates a new game with the given movement rules, gambling rules, economy, board, and players.
     *
     * @param movementRules the movement rules of the game
     * @param gamblingRules the gambling rules of the game
     * @param economy       the economy of the game
     * @param board         the board of the game
     * @param players       the players of the game, must contain 4 players
     */
    public Game(MovementRules movementRules, GamblingRules gamblingRules, Economy economy,
                Board board, List<Player> players) {
        // TODO: add parameter validation
        this.movementRules = movementRules;
        this.gamblingRules = gamblingRules;
        this.economy = economy;
        this.board = board;
        this.players = List.copyOf(players);

        turn = new Turn(players.getFirst());
    }

    /**
     * Creates a new game with the given board and players.
     *
     * @param board   the board of the game
     * @param players the players of the game, must contain 4 players
     */
    public Game(Board board, List<Player> players) {
        this(new MovementRules(board), new GamblingRules(), new Economy(), board, players);
    }

    /**
     * Creates the board and players used by the local, hardcoded game.
     * Each player starts with four pieces on the four home tiles belonging to
     * their colour.
     */
    private static DefaultSetup createDefaultSetup() {
        Board board = new Board(Tile.createAll());
        List<Player> players = List.of(
                createPlayer("Alice", PlayerColor.RED, board, 101),
                createPlayer("Charlie", PlayerColor.GREEN, board, 401),
                createPlayer("Bob", PlayerColor.BLUE, board, 301),
                createPlayer("Dave", PlayerColor.YELLOW, board, 201));
        return new DefaultSetup(board, players);
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
     * Returns the movement rules of the game.
     *
     * @return the movement rules of the game
     */
    public MovementRules getMovementRules() {
        return movementRules;
    }

    /**
     * Returns the gambling rules of the game.
     *
     * @return the gambling rules of the game
     */
    public GamblingRules getGamblingRules() {
        return gamblingRules;
    }

    /**
     * Returns the economy of the game.
     *
     * @return the economy of the game
     */
    public Economy getEconomy() {
        return economy;
    }

    /**
     * Returns the board of the game.
     *
     * @return the board of the game
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Returns the players of the game.
     *
     * @return the players of the game
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the player with the given color.
     *
     * @param color the color of the player to look up, must not be {@code null}
     * @return the player with the given color.
     */
    public Player getPlayer(PlayerColor color) {
        Objects.requireNonNull(color, "color");
        return players.stream()
                .filter(p -> p.getColor() == color)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No player with color " + color));
    }

    /**
     * Returns the current turn of the game.
     *
     * @return the current turn of the game
     */
    public Turn getTurn() {
        return turn;
    }

    /**
     * Advances the game to the next turn.
     *
     * @throws IllegalStateException if the current player is not found in the list of players,
     *                               this should never happen if the game is in a valid state
     */
    public void nextTurn() {
        int prevIndex = getPlayers().indexOf(getTurn().getPlayer());

        if (prevIndex == -1) {
            throw new IllegalStateException("Current player not found in the list of players.");
        }

        int nextIndex = (prevIndex + 1) % getPlayers().size();

        turn = new Turn(getPlayers().get(nextIndex));
    }

    private record DefaultSetup(Board board, List<Player> players) { }
}
