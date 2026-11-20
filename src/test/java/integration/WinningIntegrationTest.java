package integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Winning Integration Tests")
class WinningIntegrationTest extends BaseIntegrationTest {

    private static final int TILE_ID_FINISH_AREA = 100;
    private static final int TILE_ID_STAIRCASE_THIRD_LAST = 112;

    private void finishAllPiecesExceptFirst() {
        getPieces()
                .stream()
                .skip(1)
                .forEach(piece -> setPieceOntoTile(piece, TILE_ID_FINISH_AREA));
    }

    @Test
    @DisplayName("Player wins after finishing all pieces")
    void playerWinsAfterFinishingAllPieces() {
        finishAllPiecesExceptFirst();
        rollAndMoveFromTile(TILE_ID_STAIRCASE_THIRD_LAST, 3); // enough to finish the last piece

        assertEquals(decider(), getSnapshot().winner(),
                "Expected the current player to win after all pieces reached the finish area");
    }

    @Test
    @DisplayName("Player does not win before all pieces are finished")
    void playerDoesNotWinBeforeAllPiecesAreFinished() {
        finishAllPiecesExceptFirst();
        rollAndMoveFromTile(TILE_ID_STAIRCASE_THIRD_LAST, 1); // not enough to finish the last piece

        assertNull(getSnapshot().winner(),
                "Expected no winner while at least one piece has not reached the finish area");
    }
}
