package ch.zhaw.it.pm.humanangeryourself.domain.model;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.CardOffer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link Npc}.
 */
class NpcTest {

    private final CardOffer tryAgain = new CardOffer(CardType.TRY_AGAIN, 100);
    private final CardOffer doubleIt = new CardOffer(CardType.DOUBLE_IT, 200);
    private final Npc npc = new Npc("Trader", List.of(tryAgain, doubleIt));

    /**
     * The offer for a card type is found among all offers.
     */
    @Test
    void findsOfferByType() {
        assertSame(doubleIt, npc.findOffer(CardType.DOUBLE_IT).orElseThrow());
    }

    /**
     * There is no offer for a card the NPC doesn't sell.
     */
    @Test
    void unsoldCardHasNoOffer() {
        assertTrue(npc.findOffer(CardType.SPEED_BOOST).isEmpty());
    }
}
