package ch.zhaw.it.pm.humanangeryourself.domain.card.types;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.InvalidActionException;
import ch.zhaw.it.pm.humanangeryourself.api.action.TargetPiece;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SwitchCardTest {

    private static final int RED_TILE_ID = 5;
    private static final int BLUE_TILE_ID = 20;

    private SwitchCard card;
    private Game game;
    private Player currentPlayer;

    private Player redPlayer;
    private Player bluePlayer;
    private Piece redPiece;
    private Piece bluePiece;
    private Tile redTile;
    private Tile blueTile;

    private TargetPiece redTarget;
    private TargetPiece blueTarget;

    @BeforeEach
    void setUp() {
        card = new SwitchCard();
        game = mock(Game.class);
        currentPlayer = mock(Player.class);

        redTile = tile(RED_TILE_ID, false);
        blueTile = tile(BLUE_TILE_ID, false);
        redPiece = pieceOn(redTile);
        bluePiece = pieceOn(blueTile);
        redPlayer = player(PlayerColor.RED, redPiece);
        bluePlayer = player(PlayerColor.BLUE, bluePiece);

        when(game.getPlayers()).thenReturn(List.of(redPlayer, bluePlayer));
        when(game.getPlayer(PlayerColor.RED)).thenReturn(redPlayer);
        when(game.getPlayer(PlayerColor.BLUE)).thenReturn(bluePlayer);

        redTarget = new TargetPiece(PlayerColor.RED, RED_TILE_ID);
        blueTarget = new TargetPiece(PlayerColor.BLUE, BLUE_TILE_ID);
    }

    // ---------- helpers ----------

    private static Tile tile(int id, boolean safe) {
        Tile tile = mock(Tile.class);
        when(tile.getId()).thenReturn(id);
        when(tile.isSafe()).thenReturn(safe);
        return tile;
    }

    private static Piece pieceOn(Tile tile) {
        Piece piece = mock(Piece.class);
        when(piece.getCurrentTile()).thenReturn(tile);
        return piece;
    }

    private static Player player(PlayerColor color, Piece... pieces) {
        Player player = mock(Player.class);
        when(player.getColor()).thenReturn(color);
        when(player.getPieces()).thenReturn(List.of(pieces));
        for (Piece piece : pieces) {
            when(player.getPiece(piece.getCurrentTile().getId())).thenReturn(Optional.of(piece));
        }
        return player;
    }

    // ---------- type ----------

    @Test
    void typeIsSwitch() {
        assertEquals(CardType.SWITCH, card.type());
    }

    // ---------- requiresFurtherTarget ----------

    @Test
    void requiresFurtherTargetWithoutSelection() {
        assertTrue(card.requiresFurtherTarget(game, currentPlayer));
    }

    @Test
    void requiresFurtherTargetWithOneSelection() {
        card.setTarget(redTarget);
        assertTrue(card.requiresFurtherTarget(game, currentPlayer));
    }

    @Test
    void requiresNoFurtherTargetWithTwoSelections() {
        card.setTarget(redTarget);
        card.setTarget(blueTarget);
        assertFalse(card.requiresFurtherTarget(game, currentPlayer));
    }

    // ---------- setTarget ----------

    @Test
    void setTargetRejectsThirdTarget() {
        card.setTarget(redTarget);
        card.setTarget(blueTarget);

        assertThrows(InvalidActionException.class,
                () -> card.setTarget(new TargetPiece(PlayerColor.GREEN, 30)));
    }

    @Test
    void setTargetRejectsSamePieceTwice() {
        card.setTarget(redTarget);

        assertThrows(InvalidActionException.class, () -> card.setTarget(redTarget));
        assertTrue(card.requiresFurtherTarget(game, currentPlayer));
    }

    @Test
    void setTargetRejectsWrongActionType() {
        Action other = mock(Action.class);

        assertThrows(InvalidActionException.class, () -> card.setTarget(other));
        assertTrue(card.requiresFurtherTarget(game, currentPlayer));
    }

    // ---------- getPossibleTargets ----------

    @Test
    void getPossibleTargetsContainsAllPiecesOnUnsafeTiles() {
        assertEquals(List.of(redTarget, blueTarget), card.getPossibleTargets(game, currentPlayer));
    }

    @Test
    void getPossibleTargetsExcludesPiecesOnSafeTiles() {
        when(redTile.isSafe()).thenReturn(true);

        assertEquals(List.of(blueTarget), card.getPossibleTargets(game, currentPlayer));
    }

    @Test
    void getPossibleTargetsExcludesAlreadySelectedPieces() {
        card.setTarget(redTarget);

        assertEquals(List.of(blueTarget), card.getPossibleTargets(game, currentPlayer));
    }

    @Test
    void getPossibleTargetsIsEmptyIfNoPieceIsSwitchable() {
        when(redTile.isSafe()).thenReturn(true);
        when(blueTile.isSafe()).thenReturn(true);

        assertTrue(card.getPossibleTargets(game, currentPlayer).isEmpty());
    }

    @Test
    void getPossibleTargetsCannotBeModified() {
        assertThrows(UnsupportedOperationException.class,
                () -> card.getPossibleTargets(game, currentPlayer).add(redTarget));
    }

    // ---------- apply ----------

    @Test
    void applySwapsTilesOfBothPieces() {
        card.setTarget(redTarget);
        card.setTarget(blueTarget);

        card.apply(game, currentPlayer);

        verify(redPiece).moveTo(blueTile);
        verify(bluePiece).moveTo(redTile);
    }

    @Test
    void applyWithoutTargetsThrows() {
        assertThrows(InvalidActionException.class, () -> card.apply(game, currentPlayer));
    }

    @Test
    void applyWithOnlyOneTargetThrowsAndMovesNothing() {
        card.setTarget(redTarget);

        assertThrows(InvalidActionException.class, () -> card.apply(game, currentPlayer));
        verify(redPiece, never()).moveTo(any());
        verify(bluePiece, never()).moveTo(any());
    }

    @Test
    void applyThrowsIfPieceDoesNotExistAndMovesNothing() {
        when(bluePlayer.getPiece(BLUE_TILE_ID)).thenReturn(Optional.empty());
        card.setTarget(redTarget);
        card.setTarget(blueTarget);

        assertThrows(InvalidActionException.class, () -> card.apply(game, currentPlayer));
        verify(redPiece, never()).moveTo(any());
    }

    @Test
    void applyThrowsIfPieceStandsOnSafeTileAndMovesNothing() {
        when(blueTile.isSafe()).thenReturn(true);
        card.setTarget(redTarget);
        card.setTarget(blueTarget);

        assertThrows(InvalidActionException.class, () -> card.apply(game, currentPlayer));
        verify(redPiece, never()).moveTo(any());
        verify(bluePiece, never()).moveTo(any());
    }
}
