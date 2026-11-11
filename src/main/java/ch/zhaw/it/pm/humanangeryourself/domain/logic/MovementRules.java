package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.api.action.MovePiece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import ch.zhaw.it.pm.humanangeryourself.domain.model.TileType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Defines the movement rules for pieces on the game board.
 * <p>
 * Responsible for calculating possible moves, determining when pieces
 * enter their colour-specific staircase and handling pieces that must
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

    private static final int FINISH_AREA_ID = 100;


    private static final Random RANDOM = new Random();

    private Board board;

    /**
     * Creates a new instance of the movement rules.
     */
    public MovementRules() {
    }

    /**
     * Injects the game board into the movement rules, allowing it to access
     *
     * @param board the game board to be used for movement calculations
     */
    public void inject(Board board) {
        this.board = board;
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
     * Determines which pieces must be sent back home when the given piece
     * lands on its destination tile.
     *
     * <p>
     * Pieces on safe tiles cannot be sent back home. The identification of
     * opponent pieces on the destination tile is not yet implemented.
     *
     * @param pieceMoved the piece that has just moved
     * @return a list of pieces that must be sent back home
     */
    public List<Piece> piecesToSendBackHome(Piece pieceMoved) {
        List<Piece> piecesToSendBackHome = new ArrayList<>();
        if (pieceMoved.getCurrentTile().isSafe()) {
            return Collections.emptyList();
        } else {
            // TODO: Find all opponent pieces occupying the destination tile.
        }
        return piecesToSendBackHome;
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
            int currentTile = piece.getCurrentTile().getId();
            if (piece.getCurrentTile().getType() == TileType.HOME) {
                if (roll == 5 || roll == 6) {
                    possibleActions.add(new MovePiece(currentTile, getStartTileId(player)));
                }
            } else if (piece.getCurrentTile().getType() == TileType.TRACK
                    || piece.getCurrentTile().getType() == TileType.SAFE) {

                int dest = calculateDestinationTile(currentTile, roll);

                if (wouldEnterStaircase(currentTile, roll, player)) {
                    int endTile = getEndTileId(player);
                    int firstStaircaseTile = getFirstStaircaseTileId(player);

                    int stepsToEnd = (endTile - currentTile + 60) % 60;
                    int remainingRoll = roll - stepsToEnd;

                    int staircaseDestination = firstStaircaseTile + remainingRoll - 1;

                    possibleActions.add(new MovePiece(currentTile, staircaseDestination));
                } else {
                    possibleActions.add(new MovePiece(currentTile, dest));
                }

            } else if (piece.getCurrentTile().getType() == TileType.STAIRCASE) {
                int lastTile = getLastStaircaseTileId(player);
                int remainingSteps = lastTile - currentTile + 1;

                if (roll == remainingSteps) {
                    possibleActions.add(new MovePiece(currentTile, FINISH_AREA_ID));
                } else if (roll < remainingSteps) {
                    possibleActions.add(new MovePiece(currentTile, currentTile + roll));

                } else {
                    int destination = lastTile - (roll - remainingSteps);
                    possibleActions.add(new MovePiece(currentTile, destination));
                }
            }
        }
        return possibleActions;
    }

    /**
     * Calculates the destination tile after moving a given number of steps
     * along the main game track.
     *
     * <p>
     * The calculation accounts for the circular layout of the track,
     * where the tile after the last track tile is the first track tile.
     *
     * @param currentTile the current tile ID
     * @param roll        the number of steps to move
     * @return the calculated destination tile ID
     */
    private int calculateDestinationTile(int currentTile, int roll) {
        return (currentTile - 1 + roll) % 60 + 1;
    }

    /**
     * Checks whether a move passes the endpoint of the player's main track
     * and therefore enters their colour-specific staircase.
     *
     * @param currentTile the piece's current tile ID
     * @param roll        the number of steps to move
     * @param player      the player whose staircase is being checked
     * @return {@code true} if the move passes the track endpoint,
     *         otherwise {@code false}
     */
    private boolean wouldEnterStaircase(int currentTile, int roll, Player player) {
        int endTile = getEndTileId(player);
        int stepsToEnd = (endTile - currentTile + 60) % 60;

        return roll > stepsToEnd;
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
            case RED -> 105;
            case YELLOW -> 205;
            case BLUE -> 305;
            case GREEN -> 405;
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
            case RED -> 114;
            case YELLOW -> 214;
            case BLUE -> 314;
            case GREEN -> 414;
        };
    }
}
