package integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.action.Gamble;
import ch.zhaw.it.pm.humanangeryourself.api.action.PlayCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.Roll;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ActionCard Integration Test")
public class ActionCardIntegrationTest extends BaseIntegrationTest {
    private void rushThroughRound() {
        rushThroughTurn();
        rushThroughTurn();
        rushThroughTurn();
        rushThroughTurn();
    }

    private void giveCurrentPlayerCard(CardType cardType) {
        getCurrentPlayer().addCard(cardType);

        // so that the available actions are recalculated
        rushThroughRound();
    }

    private void playCard(CardType cardType) {
        submitChecked(new PlayCard(cardType));
    }

    private void currentPlayerPlayCard(CardType cardType) {
        giveCurrentPlayerCard(cardType);
        playCard(cardType);
    }

    @Nested
    @DisplayName("General Card Rules")
    class GeneralCardRules {
        @Test
        @DisplayName("Cards may only be played during step one")
        void cardsMayOnlyBePlayedDuringStepOne() {
            // Arrange
            giveCurrentPlayerCard(CardType.DOUBLE_IT);

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
            giveCurrentPlayerCard(CardType.DOUBLE_IT);
            giveCurrentPlayerCard(CardType.DOUBLE_IT);

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

    @Nested
    @DisplayName("Hand Cards")
    class HandCards {
        @ParameterizedTest
        @DisplayName("Hand cards are consumed after use")
        @EnumSource(value = CardType.class,
                names = {"SPEED_BOOST", "TRY_AGAIN", "MONEY_TIGER", "DOUBLE_IT", "SWITCH"})
        void handCardsAreConsumedAfterUse(CardType card) {
            currentPlayerPlayCard(card);

            assertFalse(getSnapshot().viewerCards().contains(card),
                    "Expected " + card + " card to be consumed after use");
        }

        @Test
        @DisplayName("Speed Boost doubles next movement")
        void speedBoost() {
            giveCurrentPlayerCard(CardType.SPEED_BOOST);
            setRollDieResult(5);

            playCard(CardType.SPEED_BOOST);
            submitChecked(new Roll());

            assertEquals(10, ((PrimaryActionResult.RollResult) getSnapshot().primaryActionResult()).dieRoll(),
                    "Expected Speed Boost to double the next movement");
        }

        @Test
        @DisplayName("Try Again guarantees gambling card result")
        void tryAgain() {
            currentPlayerPlayCard(CardType.TRY_AGAIN);

            submitChecked(new Gamble());

            assertInstanceOf(PrimaryActionResult.GambleResult.CardResult.class,
                    getSnapshot().primaryActionResult(),
                    "Expected Try Again to guarantee a gambling card result");
        }

        @Nested
        @DisplayName("Money Tiger")
        class MoneyTiger {
            @Test
            @DisplayName("Money Tiger guarantees gambling payout")
            void moneyTigerGuaranteesGamblingPayout() {
                currentPlayerPlayCard(CardType.MONEY_TIGER);

                submitChecked(new Gamble());

                assertInstanceOf(PrimaryActionResult.GambleResult.MoneyResult.class,
                        getSnapshot().primaryActionResult(),
                        "Expected Money Tiger to guarantee a gambling payout");
            }

            @Test
            @DisplayName("Money Tiger doubles gambling payout")
            void moneyTigerDoublesGamblingPayout() {
                setGambleResult(1000);

                currentPlayerPlayCard(CardType.MONEY_TIGER);

                submitChecked(new Gamble());

                long actualAmount = ((PrimaryActionResult.GambleResult.MoneyResult) getSnapshot().primaryActionResult()).amount();
                assertEquals(2000, actualAmount,
                        "Expected Money Tiger to double the gambling payout");
            }
        }

        @Nested
        @DisplayName("Double It")
        class DoubleIt {
            private static final int DEBT = 1000;
            private static final PlayerColor NEXT_PLAYER = PlayerColor.YELLOW;

            @BeforeEach
            void setup() {
                getCurrentPlayer().resetBalanceToZero();
                getCurrentPlayer().changeBalanceBy(-DEBT);

                giveCurrentPlayerCard(CardType.DOUBLE_IT);
            }

            @Test
            @DisplayName("Double It transfers double the debt to the next player")
            void doubleItTransfersDebtToNextPlayer() {
                long moneyBefore = getPlayerInfo(NEXT_PLAYER).money();

                currentPlayerPlayCard(CardType.DOUBLE_IT);

                long moneyAfter = getPlayerInfo(NEXT_PLAYER).money();

                assertEquals(moneyBefore - DEBT * 2, moneyAfter,
                        "Expected Double It to transfer double the debt to the next player");
            }

            @Test
            @DisplayName("Double It sets player balance to zero")
            void doubleItSetsPlayerBalanceToZero() {
                currentPlayerPlayCard(CardType.DOUBLE_IT);

                long balanceAfter = getDeciderInfo().money();

                assertEquals(0, balanceAfter,
                        "Expected Double It to set the player's balance to zero");
            }
        }

        // TODO: implement
        @Nested
        @Disabled
        @DisplayName("Switch")
        class Switch {
            @Test
            @DisplayName("Switch works on normal Tiles")
            void switchWorksOnNormalTiles() {

            }

            @Test
            @DisplayName("Switch does not work on Safe Tiles")
            void switchDoesNotWorkOnSafeTiles() {

            }
        }

        // TODO: implement
        @Nested
        @Disabled
        @DisplayName("Anger Reverse")
        class AngerReverse {
            @Test
            @DisplayName("Anger Reverse counters Double It")
            void angerReverseCountersDoubleIt() {
            }

            @Test
            @DisplayName("Anger Reverse counters Switch")
            void angerReverseCountersSwitch() {
            }

            @Test
            @DisplayName("Anger Reverse counters another Anger Reverse")
            void angerReverseCountersAnotherAngerReverse() {
            }
        }
    }

    @Nested
    @DisplayName("Instant Cards")
    class InstantCards {
        @Test
        @DisplayName("Jackpot grants money when drawn")
        void jackpotGrantsMoneyWhenDrawn() {
            final long jackpotAmount = 250000L;

            long balanceBefore = getDeciderInfo().money();

            setGambleResult(CardType.JACKPOT);

            submitChecked(new Gamble());

            long balanceAfter = getDeciderInfo().money();

            assertEquals(balanceBefore + jackpotAmount, balanceAfter,
                    "Expected Jackpot to grant " + jackpotAmount + " money when drawn");
        }

        @Test
        @DisplayName("Charity removes money when drawn")
        void charityRemovesMoneyWhenDrawn() {
            final long charityAmount = 100000L;

            long balanceBefore = getDeciderInfo().money();

            setGambleResult(CardType.CHARITY);

            submitChecked(new Gamble());

            long balanceAfter = getDeciderInfo().money();

            assertEquals(balanceBefore - charityAmount, balanceAfter,
                    "Expected Charity to remove " + charityAmount + " money when drawn");
        }
    }
}
