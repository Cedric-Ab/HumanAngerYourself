package ch.zhaw.it.pm.humanangeryourself.domain.game;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.Economy;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.GamblingRules;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.MovementRules;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a game of Human Anger Yourself, containing the board, players, and the current turn.
 */
public class Game {
    private final @NotNull MovementRules movementRules;
    private final @NotNull GamblingRules gamblingRules;
    private final @NotNull Economy economy;

    private final Board board;
    private final List<Player> players;
    private final MovementRules movementRules;
    private final GamblingRules gamblingRules;
    private final Economy economy;

    private Turn turn;

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
        this(new MovementRules(), new GamblingRules(), new Economy(), board, players);
    }

    public Game() {
        // TODO implement properly
        this(new MovementRules(), new GamblingRules(), new Economy(), new Board(new ArrayList<>()), new ArrayList<>(List.of(
                new Player("Alice", PlayerColor.RED, 1000, new ArrayList<>()),
                new Player("Bob", PlayerColor.BLUE, 1000, new ArrayList<>()),
                new Player("Charlie", PlayerColor.GREEN, 1000, new ArrayList<>()),
                new Player("Dave", PlayerColor.YELLOW, 1000, new ArrayList<>())
        )));
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
        // TODO implement properly
        return new ArrayList<>(List.of(new Player("Bob", PlayerColor.RED, 1000, new ArrayList<>())));
    }

    /**
     * Returns the player with the given color.
     *
     * @param color the color of the player to look up, must not be {@code null}
     * @return the player with the given color.
     */
    public Player getPlayer(PlayerColor color) {
        // TODO implement properly
        return new Player("Bob", PlayerColor.RED, 1000, new ArrayList<>());
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
}
