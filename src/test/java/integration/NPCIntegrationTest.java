package integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ch.zhaw.it.pm.humanangeryourself.api.action.BuyCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.EndTurn;
import ch.zhaw.it.pm.humanangeryourself.api.action.Gamble;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("NPC Integration Test")
public class NPCIntegrationTest extends BaseIntegrationTest {
    // TODO: change to tile that actually has an NPC on it
    private static final int TILE_ID_NPC = 5;

    private List<BuyCard> setupEndOfTurnPhase(int balance) {
        setPieceOntoTile(getFirstPiece(), TILE_ID_NPC);
        setGambleResult(balance);
        submitChecked(new Gamble());

        return getAvailablePurchases();
    }

    private List<BuyCard> getAvailablePurchases() {
        return availableActions().stream()
                .filter(BuyCard.class::isInstance)
                .map(BuyCard.class::cast)
                .toList();
    }

    @Test
    @DisplayName("Can buy Card")
    void canBuyCard() {
        List<BuyCard> availablePurchases = setupEndOfTurnPhase(1_000_000);

        assertFalse(availablePurchases.isEmpty(),
                "Expected to be able to buy a card, but no BuyCard action was available.");
    }

    @Test
    @DisplayName("Cannot buy while in Dept")
    void cannotBuyWhileInDept() {
        List<BuyCard> availablePurchases = setupEndOfTurnPhase(-1);

        assertTrue(availablePurchases.isEmpty(),
                "Expected not to be able to buy a card while in dept, " +
                        "but a BuyCard action was available.");
    }

    @Test
    @DisplayName("Cannot buy if it would result in debt")
    void cannotBuyIfItWouldResultInDebt() {
        List<BuyCard> availablePurchases = setupEndOfTurnPhase(1);

        assertTrue(availablePurchases.isEmpty(),
                "Expected not to be able to buy a card if it would result in debt, " +
                        "but a BuyCard action was available.");
    }

    @Test
    @DisplayName("Buying a Hand Card")
    void buyingHandCard() {
        // TODO: make sure the card is a Hand Card
        BuyCard purchase = setupEndOfTurnPhase(1_000_000).getFirst();

        submitChecked(purchase);

        assertTrue(getDeciderInfo().cardCount() > 0,
                "Expected to have a hand card after buying, but the hand is empty.");
        assertTrue(getSnapshot(decider()).viewerCards().contains(purchase.card()),
                "Expected to have the purchased card in hand, but it was not found.");
    }

    @Test
    @Disabled
    @DisplayName("Buying a Instant Card")
    void buyingInstantCard() {
        // TODO: make sure the card is an Instant Card
        BuyCard purchase = setupEndOfTurnPhase(1_000_000).getFirst();

        submitChecked(purchase);

        // TODO: add check to verify that the Instant Card was triggered
    }

    @Test
    @DisplayName("Can only buy once per Turn")
    void canOnlyBuyOncePerTurn() {
        // TODO: make sure the card is a Hand Card
        BuyCard purchase = setupEndOfTurnPhase(1_000_000).getFirst();

        submitChecked(purchase);

        if (!availableActions().contains(new EndTurn())) {
            throw new IllegalStateException("Test is outdated: currently not in a phase that allows buying cards");
        }

        assertTrue(getAvailablePurchases().isEmpty(),
                "Expected not to be able to buy a card after already buying one, " +
                        "but a BuyCard action was available.");
    }
}
