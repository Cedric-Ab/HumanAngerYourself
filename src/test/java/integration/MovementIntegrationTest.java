package integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.zhaw.it.pm.humanangeryourself.api.action.MovePiece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Movement Integration Tests")
public class MovementIntegrationTest extends BaseIntegrationTest {

    private void placeCurrentPlayerPiece(int tileId) {
        setPieceOntoTile(getFirstPiece(), tileId);
    }

    private void placeCurrentPlayerPieceAndRoll(int tileId, int roll) {
        placeCurrentPlayerPiece(tileId);
        rollNumber(roll);
    }

    private MovePiece getFirstMoveAction() {
        return getMoveActions().stream()
                .findFirst()
                .orElseThrow();
    }

    private int getTargetTile(int startTile, int roll) {
        placeCurrentPlayerPiece(startTile);
        rollNumber(roll);

        MovePiece move = getMoveAction(startTile);
        return move.targetTile();
    }

    @Nested
    @DisplayName("Leaving Home")
    class LeavingHome {
        private static final int TILE_ID_HOME_RED_1 = 101;
        private static final int TILE_ID_START_RED = 1;

        @ParameterizedTest
        @ValueSource(ints = {5, 6})
        @DisplayName("Piece can leave home with a roll of five or six")
        void pieceCanLeaveHome(int roll) {
            assertNotEquals(TILE_ID_HOME_RED_1, getTargetTile(TILE_ID_HOME_RED_1, roll),
                    "Expected a piece to be able to leave home with a roll of " + roll);
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4})
        @DisplayName("Piece cannot leave home with a roll below five")
        void pieceCannotLeaveHome(int roll) {
            rollNumber(roll);

            assertTrue(getMoveActions().isEmpty(),
                    "Expected no move actions to be available for a piece at home with a roll of " + roll);
        }

        @Test
        @DisplayName("Leaving home places the piece on its start field")
        void leavingHomePlacesPieceOnStartField() {
            rollNumber(5);

            MovePiece movePiece = getFirstMoveAction();
            submitChecked(movePiece);

            assertTrue(getSnapshot().pieces().stream()
                            .anyMatch(p -> p.color() == decider() && p.tile() == TILE_ID_START_RED),
                    "Expected a piece of the current player to be on the start field after leaving home");
        }

        @Test
        @DisplayName("Leaving home moves exactly one piece")
        void leavingHomeMovesExactlyOnePiece() {
            rollNumber(5);

            submitChecked(getFirstMoveAction());

            long piecesOutsideHome = getSnapshot()
                    .pieces()
                    .stream()
                    .filter(p -> p.color() == decider())
                    .filter(p -> p.tile() < 100)
                    .count();

            assertEquals(1, piecesOutsideHome);
        }
    }

    @Nested
    @DisplayName("Piece Movement")
    class PieceMovement {
        private static final int TILE_ID_LAST_TRACK_TILE = 60;

        @Test
        @DisplayName("Target tile is calculated correctly without wrap around")
        void targetTileIsCalculatedCorrectlyWithoutWrapAround() {
            assertEquals(13, getTargetTile(10, 3));
        }

        @Test
        @DisplayName("Target tile is calculated correctly with wrap around")
        void targetTileIsCalculatedCorrectlyWithWrapAround() {
            rushThroughTurn(); // skip turn as RED doesn't have to wrap around

            assertEquals(2, getTargetTile(TILE_ID_LAST_TRACK_TILE - 1, 3));
        }

        @Test
        @DisplayName("Selected piece is moved to the target tile")
        void selectedPieceIsMovedToTargetTile() {
            placeCurrentPlayerPieceAndRoll(10, 3);

            MovePiece move = getFirstMoveAction();

            submitChecked(move);

            assertEquals(move.targetTile(), getFirstPiece().getCurrentTile().getId(),
                    "Expected the piece to be moved to the target tile advertised by the action"
            );
        }

        @Test
        @DisplayName("Only the selected piece is moved")
        void onlySelectedPieceIsMoved() {
            // Arrange
            Piece firstPiece = getPieces().get(0);
            Piece secondPiece = getPieces().get(1);

            setPieceOntoTile(firstPiece, 10);
            setPieceOntoTile(secondPiece, 20);

            rollNumber(3);

            MovePiece move = availableActions().stream()
                    .filter(MovePiece.class::isInstance)
                    .map(MovePiece.class::cast)
                    .filter(action -> action.startTile() == 10)
                    .findFirst()
                    .orElseThrow();

            int secondPieceTileBefore = secondPiece.getCurrentTile().getId();

            // Act
            submitChecked(move);

            // Assert
            assertEquals(move.targetTile(), firstPiece.getCurrentTile().getId(),
                    "Expected the selected piece to move to the target tile"
            );
            assertEquals(secondPieceTileBefore, secondPiece.getCurrentTile().getId(),
                    "Expected all non-selected pieces to remain unchanged"
            );
        }

        @Test
        @DisplayName("Multiple movable pieces create multiple move options")
        void multipleMovablePiecesCreateMultipleMoveOptions() {
            // Arrange
            Piece firstPiece = getPieces().get(0);
            Piece secondPiece = getPieces().get(1);

            setPieceOntoTile(firstPiece, 10);
            setPieceOntoTile(secondPiece, 20);

            rollNumber(3);

            // Act
            List<MovePiece> moveActions = getMoveActions();

            // Assert
            assertEquals(2, moveActions.size(),
                    "Expected one move action for each movable piece"
            );

            assertTrue(moveActions.stream().anyMatch(action -> action.startTile() == 10),
                    "Expected a move option for the first piece"
            );

            assertTrue(moveActions.stream().anyMatch(action -> action.startTile() == 20),
                    "Expected a move option for the second piece"
            );
        }

        @Test
        @DisplayName("Move action references the correct piece")
        void moveActionReferencesCorrectPiece() {
            Piece firstPiece = getPieces().get(0);
            Piece secondPiece = getPieces().get(1);

            setPieceOntoTile(firstPiece, 10);
            setPieceOntoTile(secondPiece, 20);

            rollNumber(3);

            List<MovePiece> actions = getMoveActions();

            assertTrue(actions.contains(new MovePiece(10, 13)),
                    "Expected a move action for the first piece from tile 10 to tile 13");
            assertTrue(actions.contains(new MovePiece(20, 23)),
                    "Expected a move action for the second piece from tile 20 to tile 23");
        }
    }

    @Nested
    @DisplayName("Staircase and Finish Area")
    class StaircaseAndFinishArea {
        private static final int TILE_ID_FINISH_AREA = 100;
        private static final int TILE_ID_LAST_TRACK_TILE_RED = 60;
        private static final int TILE_ID_STAIRCASE_BOTTOM_RED = 105;
        private static final int TILE_ID_STAIRCASE_TOP_RED = 114;

        @Test
        @DisplayName("Piece can enter the staircase")
        void pieceCanEnterStaircase() {
            assertEquals(TILE_ID_STAIRCASE_BOTTOM_RED, getTargetTile(TILE_ID_LAST_TRACK_TILE_RED, 1),
                    "Expected a piece on the last track tile to enter the staircase with a roll of 1");
        }

        @Test
        @DisplayName("Piece can move within the staircase")
        void pieceCanMoveWithinStaircase() {
            assertEquals(TILE_ID_STAIRCASE_BOTTOM_RED + 3, getTargetTile(TILE_ID_STAIRCASE_BOTTOM_RED, 3),
                    "Expected movement within the staircase to advance by the rolled amount");
        }

        @Test
        @DisplayName("Piece bounces back at top of staircase")
        void pieceBouncesBackAtTopOfStaircase() {
            assertEquals(TILE_ID_STAIRCASE_TOP_RED - 1, getTargetTile(TILE_ID_STAIRCASE_TOP_RED, 2),
                    "Expected movement beyond the top of the staircase to bounce back");
        }

        @Test
        @DisplayName("Piece bounces back at bottom of staircase")
        void pieceBouncesBackAtBottomOfStaircase() {
            int roll = TILE_ID_STAIRCASE_TOP_RED - TILE_ID_STAIRCASE_BOTTOM_RED + 3;
            assertEquals(TILE_ID_STAIRCASE_BOTTOM_RED + 2, getTargetTile(TILE_ID_STAIRCASE_TOP_RED, roll),
                    "Expected a piece to bounce back towards the bottom of the staircase after overshooting the finish area");
        }

        @Test
        @DisplayName("Piece can enter finish area")
        void pieceCanEnterFinishArea() {
            assertEquals(TILE_ID_FINISH_AREA, getTargetTile(TILE_ID_STAIRCASE_TOP_RED - 1, 2),
                    "Expected a piece at the top of the staircase to enter the finish area with a exact roll");
        }

        @Test
        @DisplayName("Piece cannot enter finish area with depth")
        void pieceCannotEnterFinishAreaWithDepth() {
            getCurrentPlayer().resetBalanceToZero();
            getCurrentPlayer().changeBalanceBy(-1);

            assertEquals(TILE_ID_STAIRCASE_TOP_RED - 1, getTargetTile(TILE_ID_STAIRCASE_TOP_RED - 1, 2),
                    "Expected a player in debt to bounce back instead of entering the finish area");
        }

        @Test
        @DisplayName("Finished pieces cannot be moved")
        void finishedPiecesCannotBeMoved() {
            placeCurrentPlayerPieceAndRoll(TILE_ID_FINISH_AREA, 1);

            assertTrue(getMoveActions().isEmpty(),
                    "Expected no move actions to be available for a finished piece");
        }
    }

    @Nested
    @DisplayName("Capturing")
    class Capturing {
        private static final int TILE_ID_NORMAL_1 = 6;
        private static final int TILE_ID_NORMAL_2 = 9;
        private static final int TILE_ID_SAFE_SPACE = 10;

        private CaptureSetup setupCaptureSituation(
                CaptureSetup pieces, int attackerStartTile, int defenderTile
        ) {
            setPieceOntoTile(pieces.capturingPiece(), attackerStartTile);
            setPieceOntoTile(pieces.capturedPiece(), defenderTile);

            rollNumber(defenderTile - attackerStartTile);

            submitChecked(new MovePiece(attackerStartTile, defenderTile));

            return pieces;
        }

        private CaptureSetup getPiecesOfDifferentPlayer() {
            Piece defendingPiece = getFirstPiece();

            rushThroughTurn();

            Piece attackingPiece = getFirstPiece();

            return new CaptureSetup(attackingPiece, defendingPiece);
        }

        private CaptureSetup getPiecesOfSamePlayer() {
            Piece attackingPiece = getPieces().get(0);
            Piece defendingPiece = getPieces().get(1);

            return new CaptureSetup(attackingPiece, defendingPiece);
        }

        @Test
        @DisplayName("Capturing piece remains on target tile")
        void capturingPieceRemainsOnTargetTile() {
            CaptureSetup pieces = setupCaptureSituation(getPiecesOfDifferentPlayer(),
                    TILE_ID_NORMAL_1, TILE_ID_NORMAL_2);

            assertEquals(TILE_ID_NORMAL_2, pieces.capturingPiece().getCurrentTile().getId(),
                    "Expected the capturing piece to remain on the captured tile");
        }

        @Test
        @DisplayName("Captured piece returns home")
        void capturedPieceReturnsHome() {
            CaptureSetup pieces = setupCaptureSituation(getPiecesOfDifferentPlayer(),
                    TILE_ID_NORMAL_1, TILE_ID_NORMAL_2);

            assertEquals(pieces.capturedPiece().getHomeTile(), pieces.capturedPiece().getCurrentTile(),
                    "Expected the captured piece to return to its home tile");
        }

        @Test
        @DisplayName("Own pieces are not captured")
        void ownPiecesAreNotCaptured() {
            CaptureSetup pieces = setupCaptureSituation(getPiecesOfSamePlayer(),
                    TILE_ID_NORMAL_1, TILE_ID_NORMAL_2);

            assertEquals(TILE_ID_NORMAL_2, pieces.capturedPiece().getCurrentTile().getId(),
                    "Expected the stationary piece to remain on its tile");
        }

        @Test
        @DisplayName("Pieces on safe spaces are not captured")
        void piecesOnSafeSpacesAreNotCaptured() {
            CaptureSetup pieces = setupCaptureSituation(getPiecesOfDifferentPlayer(),
                    TILE_ID_NORMAL_1, TILE_ID_SAFE_SPACE);

            assertEquals(TILE_ID_SAFE_SPACE, pieces.capturedPiece().getCurrentTile().getId(),
                    "Expected a piece on a safe space to not be captured");
        }

        private record CaptureSetup(
                Piece capturingPiece,
                Piece capturedPiece
        ) {
        }
    }
}
