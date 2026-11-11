package integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.action.EndTurn;
import ch.zhaw.it.pm.humanangeryourself.api.action.Gamble;
import ch.zhaw.it.pm.humanangeryourself.api.action.InvalidActionException;
import ch.zhaw.it.pm.humanangeryourself.api.action.Roll;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Turn Integration Tests")
class TurnIntegrationTest extends BaseIntegrationTest {
    private static final PlayerColor STARTING_PLAYER = PlayerColor.RED;

    // -------------------------------------------------------------------------
    // Initial State
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("New game starts with a roll or gamble decision for the first player")
    void newGameStartsWithInitialDecision() {
        assertEquals(STARTING_PLAYER, getSnapshot().currentPlayer());

        assertTrue(availableActions().contains(new Roll()));
        assertTrue(availableActions().contains(new Gamble()));
    }

    // -------------------------------------------------------------------------
    // Turn Progression
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Ending a turn advances to the next player")
    void endingTurnAdvancesToNextPlayer() {
        PlayerColor originalPlayer = getSnapshot().currentPlayer();

        rushThroughTurn();

        PlayerColor nextPlayer = getSnapshot().currentPlayer();

        assertNotEquals(nextPlayer, originalPlayer, "Expected the next player to be different from the original player");
    }

    @Test
    @DisplayName("Turn order cycles through all players in the correct sequence")
    void turnOrderCyclesThroughAllPlayers() {
        List<PlayerColor> expectedOrder = List.of(PlayerColor.RED, PlayerColor.GREEN, PlayerColor.BLUE, PlayerColor.YELLOW);

        for (int i = 1; i <= 20; i++) {
            rushThroughTurn();

            PlayerColor expectedPlayer = expectedOrder.get(i % expectedOrder.size());
            assertEquals(expectedPlayer, getSnapshot().currentPlayer(),
                    "Expected the current player to be " + expectedPlayer);
        }
    }

    // -------------------------------------------------------------------------
    // Turn Completion Rules
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Turn cannot end before mandatory actions are completed")
    void turnCannotEndBeforeMandatoryActionsAreCompleted() {
        assertThrows(InvalidActionException.class, () -> controller.submit(decider(), new EndTurn()),
                "Expected an exception when trying to end the turn before mandatory actions are completed");
    }
}
