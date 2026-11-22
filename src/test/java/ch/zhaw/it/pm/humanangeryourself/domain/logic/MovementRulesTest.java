package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.action.MovePiece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link MovementRules} on the real game board.
 */
class MovementRulesTest {

    private Board board;
    private MovementRules rules;

    @BeforeEach
    void setUp() {
        board = new Board(Tile.createAll());
        rules = new MovementRules(board);
    }

    /**
     * Puts a single piece of the given color on a tile and returns its possible moves.
     */
    private List<MovePiece> movesFor(PlayerColor color, int tileId, int roll) {
        int homeId = switch (color) {
            case RED -> 101;
            case YELLOW -> 201;
            case BLUE -> 301;
            case GREEN -> 401;
        };
        Piece piece = new Piece(0, board.getTileById(homeId));
        piece.moveTo(board.getTileById(tileId));
        Player player = new Player("Test", color, 0, List.of(piece));
        return rules.getPossibleMoves(player, roll);
    }

    /**
     * A 5 or a 6 brings a piece from home onto its color's start tile.
     */
    @ParameterizedTest
    @CsvSource({
            "RED,    101, 1,  5",
            "YELLOW, 201, 16, 6",
            "BLUE,   301, 31, 5",
            "GREEN,  401, 46, 6"
    })
    void leavesHomeWithFiveOrSix(PlayerColor color, int home, int start, int roll) {
        assertEquals(List.of(new MovePiece(home, start)), movesFor(color, home, roll));
    }

    /**
     * Anything below 5 keeps the piece at home.
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    void staysHomeBelowFive(int roll) {
        assertTrue(movesFor(PlayerColor.RED, 101, roll).isEmpty());
    }

    /**
     * After tile 60 the track continues at tile 1.
     */
    @Test
    void wrapsAroundAfterSixty() {
        assertEquals(List.of(new MovePiece(58, 4)), movesFor(PlayerColor.YELLOW, 58, 6));
    }

    /**
     * A piece that just left home must not go straight into its staircase,
     * even though its end tile lies right behind it.
     */
    @Test
    void freshPieceSkipsStaircase() {
        assertEquals(List.of(new MovePiece(1, 7)), movesFor(PlayerColor.RED, 1, 6));
    }

    /**
     * Landing exactly on the end tile keeps the piece on the track.
     */
    @Test
    void stopsOnEndTile() {
        assertEquals(List.of(new MovePiece(58, 60)), movesFor(PlayerColor.RED, 58, 2));
    }

    /**
     * Moving past the end tile leads into the piece's own staircase.
     */
    @ParameterizedTest
    @CsvSource({
            "RED,    58, 108",
            "YELLOW, 13, 208",
            "BLUE,   28, 308",
            "GREEN,  43, 408"
    })
    void entersOwnStaircase(PlayerColor color, int current, int expected) {
        assertEquals(List.of(new MovePiece(current, expected)), movesFor(color, current, 6));
    }

    /**
     * A single step from the end tile reaches the first staircase tile.
     */
    @Test
    void entersStaircaseFromEndTile() {
        assertEquals(List.of(new MovePiece(60, 105)), movesFor(PlayerColor.RED, 60, 1));
    }

    /**
     * The exact number of steps brings the piece into the finish.
     */
    @Test
    void reachesFinishWithExactRoll() {
        assertEquals(List.of(new MovePiece(113, 100)), movesFor(PlayerColor.RED, 113, 2));
    }

    /**
     * Rolling too high makes the piece bounce back from the finish.
     */
    @Test
    void bouncesBackWhenRollTooHigh() {
        assertEquals(List.of(new MovePiece(113, 110)), movesFor(PlayerColor.RED, 113, 6));
    }

    /**
     * Bouncing back also works from the very last staircase tile.
     */
    @Test
    void bouncesBackFromLastTile() {
        assertEquals(List.of(new MovePiece(114, 109)), movesFor(PlayerColor.RED, 114, 6));
    }

    /**
     * A piece in the finish is done and can't move anymore.
     */
    @Test
    void finishedPieceCannotMove() {
        assertTrue(movesFor(PlayerColor.RED, 100, 6).isEmpty());
    }

    /**
     * Nobody gets sent home when a piece lands on a safe tile.
     */
    @Test
    void safeTileSendsNobodyHome() {
        Piece piece = new Piece(0, board.getTileById(101));
        piece.moveTo(board.getTileById(5));

        assertTrue(rules.piecesToSendBackHome(piece).isEmpty());
    }
}
