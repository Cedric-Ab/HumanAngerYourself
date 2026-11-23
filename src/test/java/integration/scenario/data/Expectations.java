package integration.scenario.data;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.NpcInfo;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PieceInfo;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PlayerInfo;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PropertyInfo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public record Expectations(
        Optional<PlayerColor> currentPlayer,
        Optional<CollectionExpectation<PlayerInfo>> players,
        Optional<CollectionExpectation<PieceInfo>> pieces,
        Optional<CollectionExpectation<PropertyInfo>> properties,
        Optional<CollectionExpectation<NpcInfo>> npcs,
        Optional<CollectionExpectation<CardType>> viewerCards,
        Optional<PrimaryActionResultExpectation> primaryActionResult,
// currently not used in scenarios, because it is not relevant for the game state
//         Optional<DecisionInfo> decision,
        Optional<PlayerColor> winner
) {
    public void verify(GameSnapshot actual) {
        currentPlayer.ifPresent(v ->
                assertEquals(v, actual.currentPlayer()));

        players.ifPresent(v ->
                v.verify(actual.players(), "players"));

        pieces.ifPresent(v ->
                v.verify(actual.pieces(), "pieces"));

        properties.ifPresent(v ->
                v.verify(actual.properties(), "properties"));

        npcs.ifPresent(v ->
                v.verify(actual.npcs(), "npcs"));

        viewerCards.ifPresent(v ->
                v.verify(actual.viewerCards(), "viewerCards"));

        primaryActionResult.ifPresent(v ->
                v.verify(actual.primaryActionResult()));

        winner.ifPresent(v ->
                assertEquals(v, actual.winner()));
    }

    public record CollectionExpectation<T>(
            MatchMode mode,
            List<T> values
    ) {
        public void verify(List<T> actual, String attributeName) {
            switch (mode) {
                case EXACT -> assertEquals(
                        values,
                        actual,
                        attributeName + " should be exactly as provided"
                );

                case CONTAINS -> {
                    List<T> remaining = new java.util.ArrayList<>(actual);

                    for (T expected : values) {
                        assertTrue(
                                remaining.remove(expected),
                                attributeName + " should contain " + expected + ". Is: " + actual
                        );
                    }
                }
                case DOES_NOT_CONTAIN -> values.forEach(forbidden ->
                        assertFalse(
                                actual.contains(forbidden),
                                attributeName + " should not contain " + forbidden
                        ));
            }
        }

        public enum MatchMode {
            EXACT,
            CONTAINS,
            DOES_NOT_CONTAIN
        }
    }

    public record PrimaryActionResultExpectation(
            Optional<Integer> dieRoll,
            Optional<Long> amount,
            Optional<CardType> card
    ) {
        public PrimaryActionResultExpectation {
            int setFields = 0;

            if (dieRoll.isPresent()) setFields++;
            if (amount.isPresent()) setFields++;
            if (card.isPresent()) setFields++;

            if (setFields != 1) {
                throw new IllegalArgumentException(
                        "Exactly one primary action result must be specified");
            }
        }

        public void verify(PrimaryActionResult actual) {
            switch (actual) {
                case PrimaryActionResult.RollResult(int actualRoll) -> {
                    if (dieRoll.isEmpty()) {
                        fail("Expected a gamble result but got a roll result");
                    }

                    assertEquals(dieRoll.get(), actualRoll);
                }

                case PrimaryActionResult.GambleResult.MoneyResult(long actualAmount) -> {
                    if (amount.isEmpty()) {
                        fail("Expected a roll result or card result but got a money result");
                    }

                    assertEquals(amount.get(), actualAmount);
                }

                case PrimaryActionResult.GambleResult.CardResult(CardType actualCard) -> {
                    if (card.isEmpty()) {
                        fail("Expected a roll result or money result but got a card result");
                    }

                    assertEquals(card.get(), actualCard);
                }
            }
        }
    }
}
