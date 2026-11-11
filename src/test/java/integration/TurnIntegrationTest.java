package integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.action.Gamble;
import ch.zhaw.it.pm.humanangeryourself.api.action.Roll;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.DecisionInfo;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.game.GameController;
import ch.zhaw.it.pm.humanangeryourself.service.SnapshotFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Turn Integration Tests")
class TurnIntegrationTest {
    private static PlayerColor STARTING_PLAYER = PlayerColor.RED;

    private GameController controller;
    private Game game;

    @BeforeEach
    void setUp() {
        // Arrange
        game = new Game();
        controller = new GameController(game);
    }

    private GameSnapshot getSnapshot() {
        return SnapshotFactory.create(controller, PlayerColor.RED);
    }

    // -------------------------------------------------------------------------
    // Initial State
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("New game starts with the first player")
    void newGameStartsWithFirstPlayer() {
        assertEquals(STARTING_PLAYER, getSnapshot().currentPlayer());
    }

    @Test
    @DisplayName("New game starts with a valid decision")
    void newGameStartsWithValidDecision() {
        DecisionInfo decision = controller.getDecision();

        assertEquals(STARTING_PLAYER, getSnapshot().currentPlayer());
        assertTrue(decision.availableActions().contains(new Roll()));
        assertTrue(decision.availableActions().contains(new Gamble()));
    }

    // -------------------------------------------------------------------------
    // Turn Progression
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Ending a turn advances to the next player")
    void endingTurnAdvancesToNextPlayer() {
    }

    @Test
    @DisplayName("Turn order cycles through all players")
    void turnOrderCyclesThroughAllPlayers() {
    }

    // -------------------------------------------------------------------------
    // Turn Ownership
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Only the active player may submit actions")
    void onlyActivePlayerMaySubmitActions() {
    }

    @Test
    @DisplayName("Player cannot act during another player's turn")
    void playerCannotActDuringAnotherPlayersTurn() {
    }

    // -------------------------------------------------------------------------
    // Decision Enforcement
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Player may only perform actions offered by the current decision")
    void playerMayOnlyPerformAvailableActions() {
    }

    @Test
    @DisplayName("Unavailable actions are rejected")
    void unavailableActionsAreRejected() {
    }

    // -------------------------------------------------------------------------
    // Consistency
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Rejected action does not change the current player")
    void rejectedActionDoesNotChangeCurrentPlayer() {
    }

    @Test
    @DisplayName("Rejected action does not change the current decision")
    void rejectedActionDoesNotChangeCurrentDecision() {
    }

    @Test
    @DisplayName("Rejected action does not modify game state")
    void rejectedActionDoesNotModifyGameState() {
    }

    // -------------------------------------------------------------------------
    // Turn Completion Rules
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Turn cannot end before mandatory actions are completed")
    void turnCannotEndBeforeMandatoryActionsAreCompleted() {
    }

    @Test
    @DisplayName("Turn can end after all mandatory actions are completed")
    void turnCanEndAfterMandatoryActionsAreCompleted() {
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private Controller createController() {
        throw new UnsupportedOperationException("test setup");
    }
}
