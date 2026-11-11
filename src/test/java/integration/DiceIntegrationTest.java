package integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.action.Roll;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.DecisionInfo;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Dice Integration Tests")
class DiceIntegrationTest extends BaseIntegrationTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    @DisplayName("Roll result appears in snapshot")
    void rollResultAppearsInSnapshot(int roll) {
        rollNumber(roll);
        assertEquals(roll, getSnapshot().lastRoll(), "The roll result should be " + roll);
    }

    @Test
    @DisplayName("Rolling changes the current decision")
    void rollingChangesCurrentDecision() {
        DecisionInfo before = getDecision();

        rollNumber(6); // assumes that the player can move a piece after rolling a 6

        assertNotEquals(before, getDecision(),
                "Expected rolling the die to change the current decision");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    @DisplayName("All dice values from one to six are handled")
    void allDiceValuesFromOneToSixAreHandled(int roll) {
        setRollDieResult(roll);

        assertDoesNotThrow(
                () -> submitValid(new Roll()),
                "Expected dice value " + roll + " to be handled correctly"
        );
    }

    @Test
    @DisplayName("Rolling six grants another roll")
    void rollingSixGrantsAnotherRoll() {
        rollNumber(6);

        assertTrue(availableActions().contains(new Roll()),
                "Expected another roll to be available after rolling six");

        assertEquals(PlayerColor.RED, getSnapshot().currentPlayer(),
                "Expected the same player to keep the turn after rolling six");
    }
}
