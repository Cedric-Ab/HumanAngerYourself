package integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Winning Integration Tests")
class WinningIntegrationTest extends BaseIntegrationTest {

    private static final int TILE_ID_FINISH_AREA = 100;
    private static final int TILE_ID_STAIRCASE_THIRD_LAST = 112;

    private List<Piece> getPieces() {
        return new ArrayList<>(
                getGame()
                        .getPlayer(decider())
                        .getPieces()
        );
    }

    private void setPieceOntoTile(Piece piece, int tileId) {
        Tile tile = getGame()
                .getBoard()
                .getTileById(tileId);

        piece.moveTo(tile);
    }

    private void rollAndMove(int roll) {
        rollNumber(roll);
        submitChecked(availableActions().getFirst());
    }

    private void finishAllPiecesExceptOne() {
        List<Piece> pieces = getPieces();
        setPieceOntoTile(pieces.removeFirst(), TILE_ID_STAIRCASE_THIRD_LAST);
        pieces.forEach(piece -> setPieceOntoTile(piece, TILE_ID_FINISH_AREA));
    }

    @Test
    @DisplayName("Player wins after finishing all pieces")
    void playerWinsAfterFinishingAllPieces() {
        finishAllPiecesExceptOne();

        rollAndMove(3); // enough to finish the last piece

        assertEquals(decider(), getSnapshot().winner(),
                "Expected the current player to win after all pieces reached the finish area");
    }

    @Test
    @DisplayName("Player does not win before all pieces are finished")
    void playerDoesNotWinBeforeAllPiecesAreFinished() {
        finishAllPiecesExceptOne();

        rollAndMove(1); // not enough to finish the last piece

        assertNull(getSnapshot().winner(),
                "Expected no winner while at least one piece has not reached the finish area");
    }
}
