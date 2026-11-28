package ch.zhaw.it.pm.humanangeryourself.domain.model;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.domain.card.Card;
import ch.zhaw.it.pm.humanangeryourself.domain.card.JackpotCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.SwitchCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.TryAgainCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link Player}.
 */
class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Alice", PlayerColor.RED, 1_000, List.of());
    }

    /**
     * Zero money is fine, only below zero counts as debt.
     */
    @Test
    void inDebtOnlyBelowZero() {
        player.changeBalanceBy(-1_000);
        assertFalse(player.isInDebt());

        player.changeBalanceBy(-1);
        assertTrue(player.isInDebt());
    }

    /**
     * Removing a card takes away just one copy, not all of them.
     */
    @Test
    void removesOnlyOneCopy() {
        Card first = new SwitchCard();
        Card second = new SwitchCard();
        player.addCard(first);
        player.addCard(second);

        player.removeCard(first);

        assertEquals(List.of(second), player.getCards());
    }

    /**
     * Removing a card the player doesn't have fails.
     */
    @Test
    void removingMissingCardFails() {
        assertThrows(IllegalStateException.class, () -> player.removeCard(new JackpotCard()));
    }

    /**
     * A card is found by its type, even when the player holds other cards too.
     */
    @Test
    void findsCardByType() {
        Card tryAgain = new TryAgainCard();
        player.addCard(new SwitchCard());
        player.addCard(tryAgain);

        assertSame(tryAgain, player.getCard(CardType.TRY_AGAIN));
    }

    /**
     * Asking for a type the player doesn't hold fails.
     */
    @Test
    void missingCardTypeFails() {
        player.addCard(new SwitchCard());

        assertThrows(IllegalStateException.class, () -> player.getCard(CardType.TRY_AGAIN));
    }

    /**
     * The player has a card from when it is added until it is removed.
     */
    @Test
    void hasCardUntilRemoved() {
        Card card = new SwitchCard();
        assertFalse(player.hasCard(card));

        player.addCard(card);
        assertTrue(player.hasCard(card));

        player.removeCard(card);
        assertFalse(player.hasCard(card));
    }

    /**
     * A missing card can't be added to the hand.
     */
    @Test
    void rejectsNullCard() {
        assertThrows(NullPointerException.class, () -> player.addCard(null));
    }

    /**
     * The player finds their piece on the tile it currently stands on.
     */
    @Test
    void findsPieceOnTile() {
        Tile home = new Tile(101, TileType.HOME, 0, null);
        Tile track = new Tile(11, TileType.TRACK, 0, null);
        Piece piece = new Piece(0, home);
        Player withPiece = new Player("Bob", PlayerColor.BLUE, 0, List.of(piece));

        assertTrue(withPiece.getPiece(track).isEmpty());
        piece.moveTo(track);
        assertSame(piece, withPiece.getPiece(track).orElseThrow());
    }

    /**
     * All pieces on the same tile are found, but none from other tiles.
     */
    @Test
    void findsAllPiecesOnTile() {
        Tile track = new Tile(11, TileType.TRACK, 0, null);
        Piece first = new Piece(0, new Tile(101, TileType.HOME, 0, null));
        Piece second = new Piece(1, new Tile(102, TileType.HOME, 0, null));
        Piece atHome = new Piece(2, new Tile(103, TileType.HOME, 0, null));
        first.moveTo(track);
        second.moveTo(track);
        Player withPieces = new Player("Bob", PlayerColor.BLUE, 0, List.of(first, second, atHome));

        assertEquals(List.of(first, second), withPieces.getPieces(track));
    }
}
