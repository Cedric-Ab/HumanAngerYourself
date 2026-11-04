package ch.zhaw.it.pm.humanangeryourself.domain.game;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a game of Human Anger Yourself, containing the board, players, and the current turn.
 */
public class Game {
    private final Board board;
    private final List<Player> players;

    private Turn turn;

    /**
     * Creates a new game with the given board and players.
     *
     * @param board   the board of the game
     * @param players the players of the game, must contain 4 players
     */
    public Game(Board board, List<Player> players) {
        // TODO: add parameter validation
        this.board = board;
        this.players = List.copyOf(players);

        turn = new Turn();
    }

    public Game() {
        // TODO implement properly
        this(new Board(new ArrayList<>()), new ArrayList<>(List.of(
                new Player("Alice", PlayerColor.RED, 1000, new ArrayList<>()),
                new Player("Bob", PlayerColor.BLUE, 1000, new ArrayList<>()),
                new Player("Charlie", PlayerColor.GREEN, 1000, new ArrayList<>()),
                new Player("Dave", PlayerColor.YELLOW, 1000, new ArrayList<>())
        )));
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
    public @Unmodifiable List<Player> getPlayers() {
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
     */
    public void nextTurn() {
        // TODO: implement
    }
}
