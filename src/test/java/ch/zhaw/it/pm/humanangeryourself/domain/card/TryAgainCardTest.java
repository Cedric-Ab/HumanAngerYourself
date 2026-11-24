package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult.GambleResult.CardResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link TryAgainCard}.
 */
class TryAgainCardTest extends CardTestBase {

    private final TryAgainCard card = new TryAgainCard();

    /**
     * The player receives the drawn card, the others nothing.
     */
    @Test
    void addsDrawnCardToHand() {
        when(gamblingRules.spinCard()).thenReturn(new CardResult(CardType.SWITCH));

        play(card, red);

        assertEquals(List.of(CardType.SWITCH), red.getCards().stream().map(Card::type).toList());
        assertTrue(yellow.getCards().isEmpty());
    }

    /**
     * The turn shows the drawn card as result.
     */
    @Test
    void storesDrawnCardAsResult() {
        when(gamblingRules.spinCard()).thenReturn(new CardResult(CardType.SWITCH));

        play(card, red);

        assertEquals(new CardResult(CardType.SWITCH), game.getTurn().getPrimaryActionResult());
    }
}
