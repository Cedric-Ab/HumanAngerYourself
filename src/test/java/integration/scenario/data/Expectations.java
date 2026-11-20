package integration.scenario.data;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GamblingOutcome;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.NpcInfo;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PieceInfo;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PlayerInfo;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PropertyInfo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public record Expectations(
        Optional<PlayerColor> currentPlayer,
        Optional<CollectionExpectation<PlayerInfo>> players,
        Optional<CollectionExpectation<PieceInfo>> pieces,
        Optional<CollectionExpectation<PropertyInfo>> properties,
        Optional<CollectionExpectation<NpcInfo>> npcs,
        Optional<CollectionExpectation<CardType>> viewerCards,
        Optional<Integer> lastRoll,
// currently not used in scenarios, because it is not relevant for the game state
//         Optional<DecisionInfo> decision,
        Optional<GamblingOutcome> gamblingOutcome,
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

        lastRoll.ifPresent(v ->
                assertEquals(v, actual.lastRoll()));

        gamblingOutcome.ifPresent(v ->
                assertEquals(v, actual.gamblingOutcome()));

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

                case CONTAINS -> values.forEach(expected ->
                        assertTrue(
                                actual.contains(expected),
                                attributeName + " should contain " + expected + ". Is: " + actual
                        ));

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
}
