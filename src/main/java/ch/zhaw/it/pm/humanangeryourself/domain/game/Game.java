package ch.zhaw.it.pm.humanangeryourself.domain.game;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.Economy;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.GamblingRules;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.MovementRules;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import java.util.List;
import java.util.Objects;

/**
 * Represents a game of Human Anger Yourself, containing the rules (movement, gambling and economy),
 * the board, the players, and the current turn.
 */
public class Game {

    /**
     * Minimum number of players in a game.
     */
    private static final int MIN_PLAYERS = 2;
    /**
     * Maximum number of players in a game, one per color.
     */
    private static final int MAX_PLAYERS = 4;
    /**
     * Rules for moving pieces.
     */
    private final MovementRules movementRules;
    /**
     * Rules for gambling.
     */
    private final GamblingRules gamblingRules;
    /**
     * Rules for buying, rent and bankruptcy.
     */
    private final Economy economy;
    /**
     * The board the game is played on.
     */
    private final Board board;
    /**
     * The players of the game, in turn order.
     */
    private final List<Player> players;

    /**
     * The current turn, which tells whose turn it is.
     */
    private Turn turn;


    /**
     * Creates a new game with the given movement rules, gambling rules, economy, board, and players.
     * The first player in the list starts with the first turn.
     *
     * @param movementRules the movement rules of the game, must not be {@code null}
     * @param gamblingRules the gambling rules of the game, must not be {@code null}
     * @param economy       the economy of the game, must not be {@code null}
     * @param board         the board of the game, must not be {@code null}
     * @param players       the players of the game in turn order, must not be {@code null};
     *                      the list is copied and must contain between {@value #MIN_PLAYERS}
     *                      and {@value #MAX_PLAYERS} players with distinct colors
     * @throws NullPointerException     if any of the arguments is {@code null}
     * @throws IllegalArgumentException if the number of players is outside the allowed range
     *                                  or two players share the same color
     */
    public Game(MovementRules movementRules, GamblingRules gamblingRules, Economy economy,
                Board board, List<Player> players) {
        this.movementRules = Objects.requireNonNull(movementRules, "movementRules must not be null");
        this.gamblingRules = Objects.requireNonNull(gamblingRules, "gamblingRules must not be null");
        this.economy = Objects.requireNonNull(economy, "economy must not be null");
        this.board = Objects.requireNonNull(board, "board must not be null");
        this.players = List.copyOf(Objects.requireNonNull(players, "players must not be null"));
        requireValidPlayers();

        turn = new Turn(this.players.getFirst());
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
        return board;
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
     * @return the player with the given color
     * @throws NullPointerException     if {@code color} is {@code null}
     * @throws IllegalArgumentException if no player has this color
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


    /**
     * Checks that the game has between MIN_PLAYERS and MAX_PLAYERS players
     * and that every color is used only once.
     *
     * @throws IllegalArgumentException if the number of players or their colors are invalid
     */
    private void requireValidPlayers() {
        boolean validPlayerCount = players.size() >= MIN_PLAYERS && players.size() <= MAX_PLAYERS;
        long distinctColors = players.stream().map(Player::getColor).distinct().count();
        boolean colorsUnique = distinctColors == players.size();

        if (!validPlayerCount || !colorsUnique) {
            throw new IllegalArgumentException(
                    "A game needs " + MIN_PLAYERS + " to " + MAX_PLAYERS + " players with different colors");
        }
    }
}
