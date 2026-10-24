package ch.zhaw.it.pm.humanangeryourself.domain.game;

import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Represents a game of Human Anger Yourself, containing the board, players, and the current turn.
 */
public class Game {
    private final @NotNull Board board;
    private final @NotNull List<Player> players;

    private @NotNull Turn turn;

    /**
     * Creates a new game with the given board and players.
     *
     * @param board   the board of the game
     * @param players the players of the game, must contain 4 players
     */
    public Game(@NotNull Board board, @NotNull List<Player> players) {
        // TODO: add parameter validation
        this.board = board;
        this.players = List.copyOf(players);

        turn = new Turn();
    }

    /**
     * Creates a new game with a default board and 4 players.
     */
    public Game() {
        // TODO: implement properly
        this(new Board(), List.of(new Player(), new Player(), new Player(), new Player()));
    }

    /**
     * Returns the board of the game.
     *
     * @return the board of the game
     */
    public @NotNull Board getBoard() {
        return board;
    }

    /**
     * Returns the players of the game.
     *
     * @return the players of the game
     */
    public @NotNull @Unmodifiable List<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the current turn of the game.
     *
     * @return the current turn of the game
     */
    public @NotNull Turn getTurn() {
        return turn;
    }

    /**
     * Advances the game to the next turn.
     */
    public void nextTurn() {
        // TODO: implement
    }
}
