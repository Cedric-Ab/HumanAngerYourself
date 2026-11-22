package integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.action.PlayCard;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TODO: enable once the ActionCards are implemented
@Disabled
@DisplayName("ActionCard Integration Test")
public class ActionCardIntegrationTest extends BaseIntegrationTest {
    @Nested
    @DisplayName("General Card Rules")
    class GeneralCardRules {
        @Test
        @DisplayName("Cards may only be played during step one")
        void cardsMayOnlyBePlayedDuringStepOne() {
            // Arrange
            getCurrentPlayer().addCard(CardType.DOUBLE_IT);

            // Assert
            assertTrue(availableActions().stream().anyMatch(PlayCard.class::isInstance),
                    "Expected PlayCard actions to be available during step 1");

            // Act
            rushThroughPrimaryAction();

            // Assert
            assertFalse(availableActions().stream().anyMatch(PlayCard.class::isInstance),
                    "Expected PlayCard actions to be unavailable outside of step 1");
        }

        @Test
        @DisplayName("Player may play only one card per turn")
        void playerMayPlayOnlyOneCardPerTurn() {
            // Arrange
            getCurrentPlayer().addCard(CardType.DOUBLE_IT);
            getCurrentPlayer().addCard(CardType.DOUBLE_IT);

            // Act
            submitChecked(availableActions().stream()
                    .filter(PlayCard.class::isInstance)
                    .map(PlayCard.class::cast)
                    .findFirst()
                    .orElseThrow());

            // Assert
            assertFalse(availableActions().stream().anyMatch(PlayCard.class::isInstance),
                    "Expected no further PlayCard actions after a card was played this turn");
        }
    }

    // TODO: implement integration test for ActionCard, once it's clear how the api interaction should work.
}
