package ch.zhaw.it.pm.humanangeryourself.domain.model;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
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
        player.addCard(CardType.SWITCH);
        player.addCard(CardType.SWITCH);

        player.removeCard(CardType.SWITCH);

        assertEquals(List.of(CardType.SWITCH), player.getCards());
    }

    /**
     * Removing a card the player doesn't have fails.
     */
    @Test
    void removingMissingCardFails() {
        assertThrows(IllegalStateException.class, () -> player.removeCard(CardType.JACKPOT));
    }

    /**
     * The player finds their piece on the tile it currently stands on.
     */
    @Test
    void findsPieceOnTile() {
        Tile home = new Tile(101, TileType.HOME, 0, null, null);
        Tile track = new Tile(10, TileType.TRACK, 0, null, null);
        Piece piece = new Piece(0, home);
        Player withPiece = new Player("Bob", PlayerColor.BLUE, 0, List.of(piece));

        assertTrue(withPiece.getPiece(track).isEmpty());
        piece.moveTo(track);
        assertSame(piece, withPiece.getPiece(track).orElseThrow());
    }
}
