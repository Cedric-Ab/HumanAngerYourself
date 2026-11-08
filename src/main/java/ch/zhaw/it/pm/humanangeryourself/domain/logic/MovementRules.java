package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.api.action.MovePiece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;
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
    private static final int START_RED_TILE_ID = 13;
    private static final int START_GREEN_TILE_ID = 58;
    private static final int START_BLUE_TILE_ID = 43;
    private static final int START_YELLOW_TILE_ID = 28;

    private static final int END_RED_TILE_ID = 59;
    private static final int END_GREEN_TILE_ID = 44;
    private static final int END_BLUE_TILE_ID = 29;
    private static final int END_YELLOW_TILE_ID = 14;

    private static final int FINISH_AREA_ID = 100;

    private static final Random RANDOM = new Random();
    private final Board board;
    private Tile tile;

    /**
     * Creates a new instance of the movement rules.
     *
     * @param board the game board used to determine piece positions and movement
     */
    public MovementRules(Board board) {
        this.board = board;
    }

    /**
     * Checks whether a move is legal according to the game rules.
     *
     * @return {@code true} if the move is legal, otherwise {@code false}
     */
    //TODO take this out?
    public boolean legalMove() {
        return false;
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
     * <p>
     * Pieces on safe tiles cannot be sent back home.
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
     * @param player the player whose possible moves are calculated
     * @param roll the result of the die roll
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
            } else if (piece.getCurrentTile().getType() == TileType.TRACK || piece.getCurrentTile().getType() == TileType.SAFE) {
                int dest = calculateDestinationTile(currentTile, roll);
                if (wouldEnterStaircase(currentTile, dest, player)) {
                    // TODO: Determine the first staircase tile ID for the player.
                    // Add a move from the current tile to that staircase tile.
                } else {
                    possibleActions.add(new MovePiece(currentTile, dest));
                }

            } else if (piece.getCurrentTile().getType() == TileType.STAIRCASE) {
                // TODO: Determine the staircase's final tile ID.
                // TODO: Check whether the piece reaches the finish area exactly.
                // TODO: Implement bounce-back movement if the roll exceeds the remaining staircase distance.
            }
        }
        return possibleActions;
    }

    /**
     * Calculates the destination tile after moving a given number of steps
     * along the main game track.
     *
     * @param currentTile the current tile ID
     * @param roll the number of steps to move
     * @return the calculated destination tile ID
     */
    private int calculateDestinationTile(int currentTile, int roll) {
        return (currentTile + roll) % 60;
    }

    /**
     * Checks whether a move crosses the endpoint of the player's main track
     * and therefore enters their colour-specific staircase.
     *
     * @param currentTile the piece's current tile ID
     * @param destinationTile the calculated destination tile ID
     * @param player the player whose staircase is being checked
     * @return {@code true} if the move enters the player's staircase,
     * otherwise {@code false}
     */
    private boolean wouldEnterStaircase(int currentTile, int destinationTile, Player player) {
        boolean result = false;
        switch (player.getColor()) {
            case RED -> {
                if (currentTile <= END_RED_TILE_ID && destinationTile > END_RED_TILE_ID) {
                    result = true;
                }
            }
            case BLUE -> {
                if (currentTile <= END_BLUE_TILE_ID && destinationTile > END_BLUE_TILE_ID) {
                    result = true;
                }
            }
            case GREEN -> {
                if (currentTile <= END_GREEN_TILE_ID && destinationTile > END_GREEN_TILE_ID) {
                    result = true;
                }
            }
            case YELLOW -> {
                if (currentTile <= END_YELLOW_TILE_ID && destinationTile > END_YELLOW_TILE_ID) {
                    result = true;
                }
            }
        }
        return result;
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
}
