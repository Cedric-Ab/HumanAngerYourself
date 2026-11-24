package integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.action.DiscardCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.Gamble;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Gambling Integration Test")
public class GamblingIntegrationTest extends BaseIntegrationTest {

    private PrimaryActionResult.GambleResult getGambleResult() {
        if (getSnapshot().primaryActionResult() instanceof PrimaryActionResult.GambleResult gambleResult) {
            return gambleResult;
        } else {
            throw new IllegalStateException("PrimaryActionResult is not a GambleResult");
        }
    }

    @Nested
    @DisplayName("Monetary Reward")
    class MonetaryReward {
        @ParameterizedTest
        @DisplayName("Correspondig Gambling Outcome is present")
        @ValueSource(ints = {-1_000_000, -1, 0, 1, 1_000_000})
        void gamblingOutcomeIsPresent(long moneyChange) {
            setGambleResult(moneyChange);

            submitChecked(new Gamble());

            assertEquals(new PrimaryActionResult.GambleResult.MoneyResult(moneyChange), getGambleResult(),
                    "Expected Gambling Outcome to be present in PrimaryActionResult");
        }

        @ParameterizedTest
        @DisplayName("Money of Player is changed")
        @ValueSource(ints = {-1_000_000, -1, 0, 1, 1_000_000})
        void moneyOfPlayerIsChanged(long moneyChange) {
            setGambleResult(moneyChange);

            long moneyBefore = getDeciderInfo().money();

            submitChecked(new Gamble());

            long moneyAfter = getDeciderInfo().money();

            assertEquals(moneyBefore + moneyChange, moneyAfter,
                    "Expected Money of Player to be changed by the amount of the Gambling Outcome");
        }
    }

    @Nested
    @DisplayName("Hand Card as Reward")
    class HandCard {
        @Test
        @DisplayName("Correspondig Gambling Outcome is present")
        void gamblingOutcomeIsPresent() {
            setGambleResult(CardType.SWITCH);

            submitChecked(new Gamble());

            assertEquals(new PrimaryActionResult.GambleResult.CardResult(CardType.SWITCH), getGambleResult(),
                    "Expected Gambling Outcome to be present in PrimaryActionResult");
        }

        @Test
        @DisplayName("Hand Card is added to Players Hand Cards")
        void handCardIsAddedToPlayersHandCards() {
            setGambleResult(CardType.SWITCH);

            int handCardsBefore = getDeciderInfo().cardCount();

            submitChecked(new Gamble());

            int handCardsAfter = getDeciderInfo().cardCount();

            assertEquals(handCardsBefore + 1, handCardsAfter,
                    "Expected a Hand Card to be added to Players Hand Cards");
            assertTrue(getSnapshot().viewerCards().contains(CardType.SWITCH),
                    "Expected Hand Card to be present in Players Hand Cards");
        }

        // TODO: enable this test once discard is implemented
        @Disabled
        @Test
        @DisplayName("Exceding Hand Limit triggers Hand Card Discard")
        void handCardDiscard() {
            final int handLimit = 10;

            setGambleResult(CardType.SWITCH);

            // Fill Hand Cards to the limit
            for (int i = 0; i < handLimit; i++) {
                getCurrentPlayer().addCard(Game.cardFromType(CardType.SWITCH));
            }

            submitChecked(new Gamble());

            assertTrue(availableActions().contains(new DiscardCard(CardType.SWITCH)),
                    "Expected Hand Card Discard to be available when Hand Limit is exceeded");
            assertTrue(availableActions().stream().noneMatch(DiscardCard.class::isInstance),
                    "Expected Hand Card Discard to be the only available action when Hand Limit is exceeded");
        }
    }

    @Nested
    @DisplayName("Instant Card as Reward")
    class InstantCard {
        @Test
        @DisplayName("Correspondig Gambling Outcome is present")
        void gamblingOutcomeIsPresent() {
            setGambleResult(CardType.JACKPOT);

            submitChecked(new Gamble());

            assertEquals(new PrimaryActionResult.GambleResult.CardResult(CardType.JACKPOT), getGambleResult(),
                    "Expected Gambling Outcome to be present in PrimaryActionResult");
        }

        // TODO: enable this test once cards are implemented
        @Disabled
        @Test
        @DisplayName("Instant Card is triggered")
        void instantCardIsTriggered() {
            setGambleResult(CardType.JACKPOT);

            long moneyBefore = getDeciderInfo().money();

            submitChecked(new Gamble());

            long moneyAfter = getDeciderInfo().money();

            assertTrue(moneyAfter > moneyBefore,
                    "Expected Instant Card to be triggered and change the Money of Player");
        }
    }
}
