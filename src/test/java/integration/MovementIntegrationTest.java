package integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.MovePiece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Movement Integration Tests")
public class MovementIntegrationTest extends BaseIntegrationTest {
    private void setPieceOntoTile(Piece piece, int tileID) {
        Tile tile = controller.getGame().getBoard().getTileById(tileID);
        piece.moveTo(tile);
    }

    @Nested
    @DisplayName("Leaving Home")
    class LeavingHome {
        private static final int START_FIELD_ID_RED = 1;

        @ParameterizedTest
        @ValueSource(ints = {5, 6})
        @DisplayName("Piece can leave home with a roll of five or six")
        void pieceCanLeaveHome(int roll) {
            rollNumber(roll);

            int homeTileID = controller.getGame().getPlayer(decider()).getPieces().getFirst().getHomeTile().getId();

            assertTrue(availableActions().stream().anyMatch(obj ->
                    obj instanceof MovePiece(int startTile, int targetTile)
                            && homeTileID == startTile
                            && startTile != targetTile
            ), "Expected a move action to be available that moves a piece from home to the start field");
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4})
        @DisplayName("Piece cannot leave home with a roll below five")
        void pieceCannotLeaveHome(int roll) {
            rollNumber(roll);

            int homeTileID = controller.getGame().getPlayer(decider()).getPieces().getFirst().getHomeTile().getId();

            assertFalse(
                    availableActions().stream().anyMatch(obj ->
                            obj instanceof MovePiece(int startTile, int targetTile)
                                    && homeTileID == startTile
                                    && startTile != targetTile
                    ), "Expected a move action to be available that moves a piece from home to the start field");
        }

        @Test
        @DisplayName("Leaving home places the piece on its start field")
        void leavingHomePlacesPieceOnStartField() {
            rollNumber(5);

            Action action = availableActions().getFirst();

            if (!(action instanceof MovePiece)) {
                throw new AssertionError("Expected a MovePiece action to be available");
            }

            submitValid(action);

            assertTrue(getSnapshot().pieces().stream()
                            .anyMatch(p -> p.color() == decider() && p.tile() == START_FIELD_ID_RED),
                    "Expected a piece of the current player to be on the start field after leaving home");
        }

        @Test
        @DisplayName("Leaving home moves exactly one piece")
        void leavingHomeMovesExactlyOnePiece() {
            rollNumber(5);

            submitValid(availableActions().getFirst());

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
        @Test
        @DisplayName("Target tile is calculated correctly without wrap around")
        void targetTileIsCalculatedCorrectlyWithoutWrapAround() {
            // Arrange
            Piece piece = controller.getGame()
                    .getPlayer(decider())
                    .getPieces()
                    .getFirst();

            setPieceOntoTile(piece, 10);

            // Act
            rollNumber(3);

            MovePiece move = availableActions().stream()
                    .filter(MovePiece.class::isInstance)
                    .map(MovePiece.class::cast)
                    .findFirst()
                    .orElseThrow();

            // Assert
            assertEquals(10, move.startTile());
            assertEquals(13, move.targetTile());
        }

        @Test
        @DisplayName("Target tile is calculated correctly with wrap around")
        void targetTileIsCalculatedCorrectlyWithWrapAround() {
            rushThroughTurn(); // skip turn as RED doesn't have to wrap around

            // Arrange
            Piece piece = controller.getGame()
                    .getPlayer(decider())
                    .getPieces()
                    .getFirst();

            int lastTrackTile = 60;

            setPieceOntoTile(piece, lastTrackTile - 1);

            // Act
            rollNumber(3);

            MovePiece move = availableActions().stream()
                    .filter(MovePiece.class::isInstance)
                    .map(MovePiece.class::cast)
                    .findFirst()
                    .orElseThrow();

            // Assert
            assertEquals(lastTrackTile - 1, move.startTile());
            assertEquals(2, move.targetTile());
        }

        @Test
        @DisplayName("Selected piece is moved to the target tile")
        void selectedPieceIsMovedToTargetTile() {
            Piece piece = controller.getGame()
                    .getPlayer(decider())
                    .getPieces()
                    .getFirst();

            setPieceOntoTile(piece, 10);

            rollNumber(3);

            MovePiece move = availableActions().stream()
                    .filter(MovePiece.class::isInstance)
                    .map(MovePiece.class::cast)
                    .findFirst()
                    .orElseThrow();

            submitValid(move);

            assertEquals(
                    move.targetTile(),
                    piece.getCurrentTile().getId(),
                    "Expected the piece to be moved to the target tile advertised by the action"
            );
        }

        @Test
        @DisplayName("Only the selected piece is moved")
        void onlySelectedPieceIsMoved() {
            // Arrange
            Piece firstPiece = controller.getGame()
                    .getPlayer(decider())
                    .getPieces()
                    .get(0);

            Piece secondPiece = controller.getGame()
                    .getPlayer(decider())
                    .getPieces()
                    .get(1);

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
            submitValid(move);

            // Assert
            assertEquals(
                    move.targetTile(),
                    firstPiece.getCurrentTile().getId(),
                    "Expected the selected piece to move to the target tile"
            );

            assertEquals(
                    secondPieceTileBefore,
                    secondPiece.getCurrentTile().getId(),
                    "Expected all non-selected pieces to remain unchanged"
            );
        }

        @Test
        @DisplayName("Multiple movable pieces create multiple move options")
        void multipleMovablePiecesCreateMultipleMoveOptions() {
            // Arrange
            Piece firstPiece = controller.getGame()
                    .getPlayer(decider())
                    .getPieces()
                    .get(0);

            Piece secondPiece = controller.getGame()
                    .getPlayer(decider())
                    .getPieces()
                    .get(1);

            setPieceOntoTile(firstPiece, 10);
            setPieceOntoTile(secondPiece, 20);

            rollNumber(3);

            // Act
            List<MovePiece> moveActions = availableActions().stream()
                    .filter(MovePiece.class::isInstance)
                    .map(MovePiece.class::cast)
                    .toList();

            // Assert
            assertEquals(
                    2,
                    moveActions.size(),
                    "Expected one move action for each movable piece"
            );

            assertTrue(
                    moveActions.stream().anyMatch(action -> action.startTile() == 10),
                    "Expected a move option for the first piece"
            );

            assertTrue(
                    moveActions.stream().anyMatch(action -> action.startTile() == 20),
                    "Expected a move option for the second piece"
            );
        }
    }

    @Nested
    @DisplayName("Staircase and Finish Area")
    class StaircaseAndFinishArea {
    }

    @Nested
    @DisplayName("Capturing")
    class Capturing {
    }
}
