package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.api.action.MovePiece;
import ch.zhaw.it.pm.humanangeryourself.config.GameConfig;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;
import ch.zhaw.it.pm.humanangeryourself.domain.model.TileType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Defines the movement rules for pieces on the game board.
 * <p>
 * Responsible for rolling the die, calculating possible moves, determining when pieces
 * enter their colour-specific staircase and determining which pieces must
 * be sent back home.
 */
public class MovementRules {
    private static final int START_RED_TILE_ID = 1;
    private static final int START_GREEN_TILE_ID = 46;
    private static final int START_BLUE_TILE_ID = 31;
    private static final int START_YELLOW_TILE_ID = 16;

    private static final int END_RED_TILE_ID = 60;
    private static final int END_GREEN_TILE_ID = 45;
    private static final int END_BLUE_TILE_ID = 30;
    private static final int END_YELLOW_TILE_ID = 15;

    private static final int STAIRCASE_RED_START_ID = 105;
    private static final int STAIRCASE_YELLOW_START_ID = 205;
    private static final int STAIRCASE_BLUE_START_ID = 305;
    private static final int STAIRCASE_GREEN_START_ID = 405;

    private static final int STAIRCASE_RED_END_ID = 114;
    private static final int STAIRCASE_YELLOW_END_ID = 214;
    private static final int STAIRCASE_BLUE_END_ID = 314;
    private static final int STAIRCASE_GREEN_END_ID = 414;

    private static final int FINISH_AREA_ID = 100;

    private static final Random RANDOM = new Random();

    private final GameConfig config;
    private final Board board;

    /**
     * Creates a new instance of the movement rules with the default configuration.
     *
     * @param board the game board used to determine piece positions and movement
     */
    public MovementRules(Board board) {
        this(GameConfig.defaults(), board);
    }

    /**
     * Creates a new instance of the movement rules with a custom configuration.
     *
     * @param config the custom configuration to use
     * @param board  the game board used to determine piece positions and movement
     */
    public MovementRules(GameConfig config, Board board) {
        this.board = board;
        this.config = config;
    }

    /**
     * Simulates a six-sided die roll.
     *
     * @return a random number between 1 and 6
     */
    public int rollDie() {
        return RANDOM.nextInt(1, 7);
    }

    /**
     * Checks if all pieces of the given player are in the finish area.
     *
     * @param player the player whose pieces are checked
     * @return {@code true} if all pieces are in the finish area, otherwise {@code false}
     */
    public boolean areAllPiecesInFinishArea(Player player) {
        return player.getPieces().stream()
                .allMatch(piece -> piece.getCurrentTile().getId() == FINISH_AREA_ID);
    }

    /**
     * Determines which pieces must be sent back home when the given piece
     * has landed on its destination tile.
     *
     * <p>
     * Pieces on safe tiles cannot be sent back home, so the result is empty in that case.
     * Otherwise, the pieces of all other players that occupy the same tile as
     * {@code pieceMoved} are returned. Pieces of the moving player are never included.
     *
     * @param pieceMoved the piece that has just moved, its current tile is the destination tile
     * @param mover      the player who moved the piece, their pieces are not sent home
     * @param players    all players of the game, including the moving player
     * @return an unmodifiable list of pieces that must be sent back home, empty if there are none
     */
    public List<Piece> piecesToSendBackHome(Piece pieceMoved, Player mover, List<Player> players) {
        Tile tile = pieceMoved.getCurrentTile();
        if (tile.isSafe()) {
            return Collections.emptyList();
        }
        return players.stream()
                .filter(player -> player.getColor() != mover.getColor())
                .flatMap(player -> player.getPieces(tile).stream())
                .toList();
    }

    /**
     * Calculates all possible moves for the given player based on the die roll.
     *
     * <p>
     * Pieces in their home area can only enter the board when rolling a 5 or 6.
     * Pieces on the main track can move to their colour-specific staircase
     * when passing its endpoint. Pieces already in the staircase move towards
     * the finish area, with excess steps causing them to move backwards.
     *
     * @param player the player whose possible moves are calculated
     * @param roll   the result of the die roll
     * @return a list of possible move actions
     */
    public List<MovePiece> getPossibleMoves(Player player, int roll) {
        List<MovePiece> possibleActions = new ArrayList<>();
        List<Piece> pieces = player.getPieces();

        for (Piece piece : pieces) {
            getPossibleMoveOfPiece(player, piece, roll).ifPresent(possibleActions::add);
        }
        return possibleActions;
    }

    /**
     * Calculates the possible move of a single piece for a given player and die roll.
     *
     * @param player the player whose possible moves are calculated
     * @param piece  the piece whose possible move is calculated
     * @param roll   the result of the die roll
     * @return an optional move action, or empty if no move is possible
     */
    public Optional<MovePiece> getPossibleMoveOfPiece(Player player, Piece piece, int roll) {
        int startTile = piece.getCurrentTile().getId();

        int intermediateDestination = startTile;
        int remainingSteps = roll;
        while (remainingSteps > 0) {
            Optional<PartialMove> result = partialMove(intermediateDestination, remainingSteps, player);
            if (result.isEmpty()) {
                return Optional.empty();
            }

            intermediateDestination = result.get().intermediateDestination();
            remainingSteps = result.get().remainingSteps();
        }

        return Optional.of(new MovePiece(startTile, intermediateDestination));
    }

    /**
     * Calculates the partial move of a piece on the board for a given player and die roll.
     * A partial move is either a complete move (uses all remaining steps) or a move that moves the piece
     * to the next stage, where movement will be different
     * (for example, from the track to start of the staircase
     * or from the middle of staircase to the top of the staircase, from where the piece will move back down).
     *
     * @param currentTile the current tile of the piece
     * @param steps       the number of remaining steps to move
     * @param player      the player whose partial move is calculated
     * @return a partial move (new position plus remaining steps), or empty if no move is possible
     */
    private Optional<PartialMove> partialMove(int currentTile, int steps, Player player) {
        TileType currentTileType = board.getTileById(currentTile).getType();

        return switch (currentTileType) {
            case HOME -> (steps == 5 || steps == 6)
                    ? Optional.of(new PartialMove(getStartTileId(player), 0)) // move onto the track
                    : Optional.empty(); // stay put
            case TRACK, SAFE -> Optional.of(partialMoveOnTrack(currentTile, steps, player));
            case STAIRCASE -> partialMoveOnStaircase(currentTile, steps, player);
            default -> Optional.empty();
        };
    }

    /**
     * Calculates the partial move of a piece on the track for a given player and remaining steps.
     *
     * @param currentTile the current tile of the piece
     * @param steps       the number of remaining steps to move
     * @param player      the player whose partial move is calculated
     * @return a partial move (new position plus remaining steps)
     */
    private PartialMove partialMoveOnTrack(int currentTile, int steps, Player player) {
        int endOfTrack = getEndTileId(player);
        int stepsToEnd = (endOfTrack - currentTile + 60) % 60;

        if (steps > stepsToEnd) { // when reaching the end of the track...
            if (player.isInDebt()) {
                // ...stick to the bottom of the staircase
                return new PartialMove(getFirstStaircaseTileId(player), 0);
            } else {
                // ...move onto the staircase
                return new PartialMove(getFirstStaircaseTileId(player), steps - stepsToEnd - 1);
            }
        } else {
            // otherwise, simply move along the track for the remaining steps
            return new PartialMove((currentTile + steps - 1) % 60 + 1, 0);
        }
    }

    /**
     * Calculates the partial move of a piece on the staircase for a given player and remaining steps.
     *
     * @param currentTile the current tile of the piece
     * @param steps       the number of remaining steps to move
     * @param player      the player whose partial move is calculated
     * @return a partial move (new position plus remaining steps)
     */
    private Optional<PartialMove> partialMoveOnStaircase(int currentTile, int steps, Player player) {
        int topOfStaircase = getLastStaircaseTileId(player);
        int stepsToFinish = topOfStaircase - currentTile + 1; // number of steps to reach the finish area

        if (player.isInDebt()) {
            return Optional.empty(); // no moves allowed on staircase for players in debt
        } else if (steps == stepsToFinish) { // only with the exact roll and no debt...
            return Optional.of(new PartialMove(FINISH_AREA_ID, 0)); // ...may you enter the finish area
        } else if (currentTile == topOfStaircase) { // only when starting at the top of the staircase...
            steps -= 2; // ...use two steps to move onto the finish area and back
            // then move down the staircase, but stop the partial move when reaching the bottom of the staircase
            int bottomOfStaircase = getFirstStaircaseTileId(player);
            int stepsToBottom = currentTile - bottomOfStaircase;

            int stepsTaken = Math.min(steps, stepsToBottom); // number of steps this partial move will use up
            return Optional.of(new PartialMove(currentTile - stepsTaken, steps - stepsTaken));
        } else {
            // move up the staircase, but stop the partial move at the top of the staircase
            int stepsTaken = Math.min(steps, stepsToFinish - 1); // number of steps this partial move will use up
            return Optional.of(new PartialMove(currentTile + stepsTaken, steps - stepsTaken));
        }
    }

    /**
     * Returns the starting tile ID corresponding to the given player's colour.
     *
     * @param player the player whose starting tile is required
     * @return the ID of the player's starting tile
     */
    private int getStartTileId(Player player) {
        return switch (player.getColor()) {
            case RED -> START_RED_TILE_ID;
            case BLUE -> START_BLUE_TILE_ID;
            case GREEN -> START_GREEN_TILE_ID;
            case YELLOW -> START_YELLOW_TILE_ID;
        };
    }

    /**
     * Returns the endpoint tile ID of the main track for the given player's colour.
     *
     * @param player the player whose track endpoint is required
     * @return the ID of the player's track endpoint
     */
    private int getEndTileId(Player player) {
        return switch (player.getColor()) {
            case RED -> END_RED_TILE_ID;
            case YELLOW -> END_YELLOW_TILE_ID;
            case BLUE -> END_BLUE_TILE_ID;
            case GREEN -> END_GREEN_TILE_ID;
        };
    }

    /**
     * Returns the first tile ID of the staircase corresponding to the given player's colour.
     *
     * @param player the player whose staircase is required
     * @return the ID of the first staircase tile
     */
    private int getFirstStaircaseTileId(Player player) {
        return switch (player.getColor()) {
            case RED -> STAIRCASE_RED_START_ID;
            case YELLOW -> STAIRCASE_YELLOW_START_ID;
            case BLUE -> STAIRCASE_BLUE_START_ID;
            case GREEN -> STAIRCASE_GREEN_START_ID;
        };
    }

    /**
     * Returns the last tile ID of the staircase corresponding to the given player's colour.
     *
     * @param player the player whose staircase is required
     * @return the ID of the last staircase tile
     */
    private int getLastStaircaseTileId(Player player) {
        return switch (player.getColor()) {
            case RED -> STAIRCASE_RED_END_ID;
            case YELLOW -> STAIRCASE_YELLOW_END_ID;
            case BLUE -> STAIRCASE_BLUE_END_ID;
            case GREEN -> STAIRCASE_GREEN_END_ID;
        };
    }

    private record PartialMove(int intermediateDestination, int remainingSteps) { }
}
