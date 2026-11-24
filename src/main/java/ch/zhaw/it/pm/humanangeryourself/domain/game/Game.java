package ch.zhaw.it.pm.humanangeryourself.domain.game;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.config.GameConfig;
import ch.zhaw.it.pm.humanangeryourself.domain.card.AngerReverseCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.Card;
import ch.zhaw.it.pm.humanangeryourself.domain.card.CharityCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.DoubleItCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.JackpotCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.MoneyTigerCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.SpeedBoostCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.SwitchCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.TryAgainCard;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.Economy;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.GamblingRules;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.MovementRules;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
     * Configuration of the game, containing various settings and parameters.
     */
    private final GameConfig config;
    /**
     * Rules for moving pieces.
     */
    private final MovementRules movementRules;
    /**
     * Rules for gambling.
     */
    private final GamblingRules gamblingRules;
    /**
     * Rules for buying and rent.
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
     * The winner of the game, if there is one. If the game is still ongoing, this is {@code null}.
     */
    private Player winner = null;


    /**
     * Creates a new game with the given movement rules, gambling rules, economy, board, and players.
     * The first player in the list starts with the first turn.
     *
     * @param config        the configuration of the game, must not be {@code null}
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
    public Game(GameConfig config, MovementRules movementRules, GamblingRules gamblingRules, Economy economy,
                Board board, List<Player> players) {
        this.config = Objects.requireNonNull(config);
        this.movementRules = Objects.requireNonNull(movementRules);
        this.gamblingRules = Objects.requireNonNull(gamblingRules);
        this.economy = Objects.requireNonNull(economy);
        this.board = Objects.requireNonNull(board);
        this.players = List.copyOf(Objects.requireNonNull(players));
        requireValidPlayers();

        turn = new Turn(this.players.getFirst());
    }

    public static Card cardFromType(CardType type) {
        return switch (type) {
            case DOUBLE_IT -> new DoubleItCard();
            case TRY_AGAIN -> new TryAgainCard();
            case MONEY_TIGER -> new MoneyTigerCard();
            case ANGER_REVERSE -> new AngerReverseCard();
            case SPEED_BOOST -> new SpeedBoostCard();
            case SWITCH -> new SwitchCard();
            case JACKPOT -> new JackpotCard();
            case CHARITY -> new CharityCard();
        };
    }

    /**
     * Returns the configuration of the game.
     *
     * @return the configuration of the game
     */
    public GameConfig getConfig() {
        return config;
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
     * Returns the winner of the game, if there is one.
     *
     * @return an {@code Optional} containing the winner of the game,
     *         or an empty {@code Optional} if there is no winner yet
     */
    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }

    /**
     * Advances the game to the next turn.
     *
     * @throws IllegalStateException if the current player is not found in the list of players,
     *                               this should never happen if the game is in a valid state
     * @throws IllegalStateException if the game has already been won by a player
     */
    public void nextTurn() {
        if (getWinner().isPresent()) {
            throw new IllegalStateException("Cannot advance turn: the game has already been won by " + getWinner().get());
        }

        int prevIndex = getPlayers().indexOf(getTurn().getPlayer());

        if (prevIndex == -1) {
            throw new IllegalStateException("Current player not found in the list of players.");
        }

        int nextIndex = (prevIndex + 1) % getPlayers().size();

        turn = new Turn(getPlayers().get(nextIndex));
    }

    /**
     * Sets the given player as the winner of the game.
     *
     * @param player the player to set as the winner, must not be {@code null} and must be part of the game
     * @throws NullPointerException     if {@code player} is {@code null}
     * @throws IllegalArgumentException if {@code player} is not part of the game
     */
    public void setAsWinner(Player player) {
        Objects.requireNonNull(player, "player must not be null");
        if (!players.contains(player)) {
            throw new IllegalArgumentException("Player is not part of this game.");
        }
        this.winner = player;
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
