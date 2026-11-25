package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.action.MovePiece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link MovementRules} on the real game board.
 */
class MovementRulesTest {

    private Board board;
    private MovementRules rules;

    @BeforeEach
    void setUp() {
        board = new Board(Map.of());
        rules = new MovementRules(board);
    }

    /**
     * Puts a single piece of the given color on a tile and returns its possible moves.
     */
    private List<MovePiece> movesFor(PlayerColor color, int tileId, int roll) {
        Piece piece = new Piece(0, board.getHomeTiles(color).getFirst());
        piece.moveTo(board.getTileById(tileId));
        Player player = new Player("Test", color, 0, List.of(piece));
        return rules.getPossibleMoves(player, roll);
    }

    /**
     * The die shows every number from 1 to 6 and nothing else.
     */
    @Test
    void rollsOneToSix() {
        Set<Integer> rolls = Stream.generate(rules::rollDie).limit(1000).collect(Collectors.toSet());

        assertEquals(Set.of(1, 2, 3, 4, 5, 6), rolls);
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
     * Doubled rolls above 6 lead from the track into the staircase as well.
     */
    @ParameterizedTest
    @CsvSource({
            "55, 8,  107",
            "58, 12, 114",
            "60, 10, 114"
    })
    void entersStaircaseWithHighRoll(int current, int roll, int expected) {
        assertEquals(List.of(new MovePiece(current, expected)), movesFor(PlayerColor.RED, current, roll));
    }

    /**
     * Inside the staircase rolls above 6 move and finish like any other roll.
     */
    @ParameterizedTest
    @CsvSource({
            "105, 8,  113",
            "105, 10, 100"
    })
    void movesInStaircaseWithHighRoll(int current, int roll, int expected) {
        assertEquals(List.of(new MovePiece(current, expected)), movesFor(PlayerColor.RED, current, roll));
    }

    /**
     * The exact number of steps brings the piece into the finish.
     */
    @Test
    void reachesFinishWithExactRoll() {
        assertEquals(List.of(new MovePiece(113, 100)), movesFor(PlayerColor.RED, 113, 2));
    }

    /**
     * A player has only won when every single piece is in the finish.
     */
    @Test
    void allPiecesMustBeInFinish() {
        Tile finish = board.getTileById(100);
        Piece first = new Piece(0, board.getTileById(101));
        Piece second = new Piece(1, board.getTileById(102));
        Player player = new Player("Test", PlayerColor.RED, 0, List.of(first, second));
        assertFalse(rules.areAllPiecesInFinishArea(player));

        first.moveTo(finish);
        assertFalse(rules.areAllPiecesInFinishArea(player));

        second.moveTo(finish);
        assertTrue(rules.areAllPiecesInFinishArea(player));
    }

    /**
     * Rolling too high makes the piece bounce back from the finish.
     */
    @Disabled("Bug: the piece bounces back one tile too far, see issue 115")
    @Test
    void bouncesBackWhenRollTooHigh() {
        assertEquals(List.of(new MovePiece(113, 111)), movesFor(PlayerColor.RED, 113, 6));
    }

    /**
     * Bouncing back also works from the very last staircase tile.
     */
    @Disabled("Bug: the piece bounces back one tile too far, see issue 115")
    @Test
    void bouncesBackFromLastTile() {
        assertEquals(List.of(new MovePiece(114, 110)), movesFor(PlayerColor.RED, 114, 6));
    }

    /**
     * Rolls above 6 bounce back from the finish like any other roll.
     */
    @Disabled("Bug: the piece bounces back one tile too far, see issue 115")
    @Test
    void bouncesBackWithHighRoll() {
        assertEquals(List.of(new MovePiece(107, 111)), movesFor(PlayerColor.RED, 107, 12));
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
        Piece moved = new Piece(0, board.getTileById(101));
        Piece other = new Piece(1, board.getTileById(301));
        List.of(moved, other).forEach(piece -> piece.moveTo(board.getTileById(5)));
        Player mover = new Player("Mover", PlayerColor.RED, 0, List.of(moved));
        Player opponent = new Player("Opponent", PlayerColor.BLUE, 0, List.of(other));

        assertTrue(rules.piecesToSendBackHome(moved, mover, List.of(mover, opponent)).isEmpty());
    }

    /**
     * On a normal tile opponents are sent home, but the player's own pieces stay.
     */
    @Test
    void sendsOnlyOpponentsHome() {
        Piece moved = new Piece(0, board.getTileById(101));
        Piece own = new Piece(1, board.getTileById(102));
        Piece other = new Piece(2, board.getTileById(301));
        List.of(moved, own, other).forEach(piece -> piece.moveTo(board.getTileById(7)));
        Player mover = new Player("Mover", PlayerColor.RED, 0, List.of(moved, own));
        Player opponent = new Player("Opponent", PlayerColor.BLUE, 0, List.of(other));

        assertEquals(List.of(other), rules.piecesToSendBackHome(moved, mover, List.of(mover, opponent)));
    }
}
