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
import ch.zhaw.it.pm.humanangeryourself.api.action.TargetPiece;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TODO: enable these tests once action cards are implemented
@Disabled
@DisplayName("ActionCard Integration Test")
public class ActionCardIntegrationTest extends BaseIntegrationTest {
    private void rushThroughRound() {
        rushThroughTurn();
        rushThroughTurn();
        rushThroughTurn();
        rushThroughTurn();
    }

    private void giveCurrentPlayerCard(CardType cardType) {
        getCurrentPlayer().addCard(Game.cardFromType(cardType));

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

        @Nested
        @DisplayName("Switch")
        class Switch {
            private static final PlayerColor OTHER_PLAYER = PlayerColor.GREEN;

            private static final int TILE_ID_NORMAL_TILE_1 = 3;
            private static final int TILE_ID_NORMAL_TILE_2 = 8;
            private static final int TILE_ID_SAFE_TILE = 8;

            private SwitchSetup setupSwitchSituation(int ownTile, int targetTile) {
                SwitchSetup setup = new SwitchSetup(getFirstPiece(),
                        getGame().getPlayer(OTHER_PLAYER).getPieces().getFirst());

                setup.ownPiece().moveTo(getGame().getBoard().getTileById(ownTile));
                setup.targetPiece().moveTo(getGame().getBoard().getTileById(targetTile));

                return setup;
            }

            @Test
            @DisplayName("Switch works on normal Tiles")
            void switchWorksOnNormalTiles() {
                SwitchSetup setup = setupSwitchSituation(TILE_ID_NORMAL_TILE_1, TILE_ID_NORMAL_TILE_2);

                giveCurrentPlayerCard(CardType.SWITCH);

                TargetPiece targetOwn = setup.targetOwn(decider());
                assertTrue(availableActions().contains(targetOwn),
                        "Expected Switch action to be available for own piece on normal tile");
                submitChecked(targetOwn);

                TargetPiece targetOther = setup.targetOther(OTHER_PLAYER);
                assertTrue(availableActions().contains(targetOther),
                        "Expected Switch action to be available for other player's piece on normal tile");
                submitChecked(targetOther);

                assertEquals(TILE_ID_NORMAL_TILE_2, setup.targetPiece().getCurrentTile().getId(),
                        "Expected own piece to be on the target piece's tile after switch");
                assertEquals(TILE_ID_NORMAL_TILE_1, setup.ownPiece().getCurrentTile().getId(),
                        "Expected target piece to be on the own piece's tile after switch");
            }

            @Test
            @DisplayName("Switch does not work on Safe Tiles")
            void switchDoesNotWorkOnSafeTiles() {
                SwitchSetup setup = setupSwitchSituation(TILE_ID_NORMAL_TILE_1, TILE_ID_SAFE_TILE);

                giveCurrentPlayerCard(CardType.SWITCH);

                TargetPiece targetOwn = setup.targetOwn(decider());
                assertTrue(availableActions().contains(targetOwn),
                        "Expected Switch action to be available for own piece on normal tile");
                submitChecked(targetOwn);

                TargetPiece targetOther = setup.targetOther(OTHER_PLAYER);
                assertFalse(availableActions().contains(targetOther),
                        "Expected Switch action to be unavailable for other player's piece on safe tile");
            }

            private record SwitchSetup(
                    Piece ownPiece,
                    Piece targetPiece
            ) {
                private TargetPiece targetOwn(PlayerColor player) {
                    return new TargetPiece(player, targetPiece().getId());
                }

                private TargetPiece targetOther(PlayerColor player) {
                    return new TargetPiece(player, ownPiece().getId());
                }
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
