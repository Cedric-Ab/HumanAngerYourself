package integration;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.DiscardCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.EndTurn;
import ch.zhaw.it.pm.humanangeryourself.api.action.InvalidActionException;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.CardOffer;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.NpcInfo;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for buying action cards from NPCs.
 * <p>
 * Uses the default NPCs from {@link ch.zhaw.it.pm.humanangeryourself.config.NpcConfig#defaults()}:
 * "Grumpy Old Woman" on tile 10 sells SPEED_BOOST (20'000) and SWITCH (40'000),
 * "Honest Salesman" on tile 25 sells TRY_AGAIN (30'000) and MONEY_TIGER (50'000).
 * Every player starts with 100'000.
 */
@DisplayName("NPC shops")
class NPCIntegrationTest extends BaseIntegrationTest {
    private static final int GRUMPY_OLD_WOMAN_TILE = 10;
    private static final int HONEST_SALESMAN_TILE = 25;
    private static final int TRACK_TILE_WITHOUT_NPC = 3;

    private static final BuyCard BUY_SPEED_BOOST = new BuyCard(GRUMPY_OLD_WOMAN_TILE, CardType.SPEED_BOOST);
    private static final BuyCard BUY_SWITCH = new BuyCard(GRUMPY_OLD_WOMAN_TILE, CardType.SWITCH);

    @Test
    @DisplayName("Snapshot contains every NPC with its name and offers, tiles without an NPC have none.")
    void snapshotContainsNpcsWithNameAndOffers() {
        NpcInfo grumpyWoman = getSnapshot().npcAt(GRUMPY_OLD_WOMAN_TILE).orElseThrow();

        assertEquals("grumpyWoman", grumpyWoman.name());
        assertEquals(Optional.of(new CardOffer(CardType.SPEED_BOOST, 20_000)), grumpyWoman.offerFor(CardType.SPEED_BOOST));
        assertTrue(grumpyWoman.offerFor(CardType.JACKPOT).isEmpty());
        assertTrue(getSnapshot().npcAt(TRACK_TILE_WITHOUT_NPC).isEmpty());
    }

    @Test
    @DisplayName("All cards of an NPC are offered when one of the players pieces stands on its tile.")
    void offersAllCardsOfNpcOnSameTileAsOwnPiece() {
        setPieceOntoTile(getFirstPiece(), GRUMPY_OLD_WOMAN_TILE);
        rushThroughPrimaryAction();

        assertEquals(List.of(BUY_SPEED_BOOST, BUY_SWITCH), getBuyCardActions());
        assertTrue(availableActions().contains(new EndTurn()));
    }

    @Test
    @DisplayName("No cards are offered when none of the players pieces stands on an NPC tile.")
    void offersNothingWithoutPieceOnNpcTile() {
        setPieceOntoTile(getFirstPiece(), TRACK_TILE_WITHOUT_NPC);
        rushThroughPrimaryAction();

        assertTrue(getBuyCardActions().isEmpty());
    }

    @Test
    @DisplayName("Pieces on several NPC tiles get the offers of all these NPCs.")
    void offersCardsOfAllNpcsThePlayerStandsOn() {
        setPieceOntoTile(getPieces().get(0), GRUMPY_OLD_WOMAN_TILE);
        setPieceOntoTile(getPieces().get(1), HONEST_SALESMAN_TILE);
        rushThroughPrimaryAction();

        assertEquals(List.of(
                BUY_SPEED_BOOST, BUY_SWITCH,
                new BuyCard(HONEST_SALESMAN_TILE, CardType.TRY_AGAIN),
                new BuyCard(HONEST_SALESMAN_TILE, CardType.MONEY_TIGER)), getBuyCardActions());
    }

    @Test
    @DisplayName("Two pieces on the same NPC tile do not offer any card twice.")
    void twoPiecesOnSameNpcTileOfferEachCardOnlyOnce() {
        setPieceOntoTile(getPieces().get(0), GRUMPY_OLD_WOMAN_TILE);
        setPieceOntoTile(getPieces().get(1), GRUMPY_OLD_WOMAN_TILE);
        rushThroughPrimaryAction();

        assertEquals(List.of(BUY_SPEED_BOOST, BUY_SWITCH), getBuyCardActions());
    }

    @Test
    @DisplayName("Buying a card deducts its price and adds the card to the players hand.")
    void buyingPaysPriceAndAddsCardToHand() {
        setPieceOntoTile(getFirstPiece(), GRUMPY_OLD_WOMAN_TILE);
        rushThroughPrimaryAction();
        long moneyBefore = getDeciderInfo().money();

        submitChecked(BUY_SPEED_BOOST);

        assertEquals(moneyBefore - 20_000, getDeciderInfo().money());
        assertEquals(List.of(CardType.SPEED_BOOST), getSnapshot(decider()).viewerCards());
    }

    @Test
    @DisplayName("Only one NPC card can be bought per turn, an second purchase is rejected.")
    void onlyOneCardCanBeBoughtPerTurn() {
        setPieceOntoTile(getFirstPiece(), GRUMPY_OLD_WOMAN_TILE);
        rushThroughPrimaryAction();

        submitChecked(BUY_SPEED_BOOST);

        assertTrue(getBuyCardActions().isEmpty());
        assertTrue(availableActions().contains(new EndTurn()));
        assertThrows(InvalidActionException.class, () -> submitUnchecked(BUY_SWITCH));
    }

    @Test
    @DisplayName("The player can buy an NPC card again in their next turn.")
    void canBuyAgainInNextOwnTurn() {
        setPieceOntoTile(getFirstPiece(), GRUMPY_OLD_WOMAN_TILE);
        rushThroughPrimaryAction();
        submitChecked(BUY_SPEED_BOOST);
        submitChecked(new EndTurn());

        int otherPlayers = getGame().getPlayers().size() - 1;
        for (int i = 0; i < otherPlayers; i++) {
            rushThroughTurn();
        }
        rushThroughPrimaryAction();

        assertEquals(List.of(BUY_SPEED_BOOST, BUY_SWITCH), getBuyCardActions());
    }

    @Test
    @DisplayName("Only cards the player can afford are offered.")
    void offersOnlyCardsThePlayerCanAfford() {
        setPieceOntoTile(getFirstPiece(), GRUMPY_OLD_WOMAN_TILE);
        getCurrentPlayer().changeBalanceBy(30_000 - getCurrentPlayer().getMoney());
        rushThroughPrimaryAction();

        assertEquals(List.of(BUY_SPEED_BOOST), getBuyCardActions());
    }

    @Test
    @DisplayName("A player in debt cannot buy any NPC card.")
    void offersNothingWhileInDebt() {
        setPieceOntoTile(getFirstPiece(), GRUMPY_OLD_WOMAN_TILE);
        getCurrentPlayer().changeBalanceBy(-getCurrentPlayer().getMoney() - 1);
        rushThroughPrimaryAction();

        assertTrue(getBuyCardActions().isEmpty());
    }

    @Test
    @DisplayName("Buying a card the NPC does not sell, or from an NPC the player does not stand on, is rejected.")
    void cardNotOfferedIsRejected() {
        setPieceOntoTile(getFirstPiece(), GRUMPY_OLD_WOMAN_TILE);
        rushThroughPrimaryAction();

        assertThrows(InvalidActionException.class,
                () -> submitUnchecked(new BuyCard(GRUMPY_OLD_WOMAN_TILE, CardType.TRY_AGAIN)));
        assertThrows(InvalidActionException.class,
                () -> submitUnchecked(new BuyCard(HONEST_SALESMAN_TILE, CardType.TRY_AGAIN)));
    }

    @Test
    @DisplayName("Buying an eleventh card forces the player to discard one before ending the turn.")
    void buyingEleventhCardRequiresDiscardingOne() {
        setPieceOntoTile(getFirstPiece(), GRUMPY_OLD_WOMAN_TILE);
        for (int i = 0; i < getGame().getConfig().maxHandSize(); i++) {
            getCurrentPlayer().addCard(Game.cardFromType(CardType.TRY_AGAIN));
        }
        rushThroughPrimaryAction();

        submitChecked(BUY_SPEED_BOOST);

        assertEquals(List.of(new DiscardCard(CardType.TRY_AGAIN), new DiscardCard(CardType.SPEED_BOOST)),
                availableActions());

        submitChecked(new DiscardCard(CardType.TRY_AGAIN));

        assertEquals(getGame().getConfig().maxHandSize(), getDeciderInfo().cardCount());
        assertTrue(availableActions().contains(new EndTurn()));
        assertFalse(availableActions().stream().anyMatch(BuyCard.class::isInstance));
    }

    private List<BuyCard> getBuyCardActions() {
        return availableActions().stream()
                .filter(BuyCard.class::isInstance)
                .map(BuyCard.class::cast)
                .toList();
    }
}
