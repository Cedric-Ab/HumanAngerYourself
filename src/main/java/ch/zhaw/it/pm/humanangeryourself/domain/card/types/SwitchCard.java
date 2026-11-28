package ch.zhaw.it.pm.humanangeryourself.domain.card.types;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.InvalidActionException;
import ch.zhaw.it.pm.humanangeryourself.api.action.TargetPiece;
import ch.zhaw.it.pm.humanangeryourself.domain.card.ApplicableCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.Targeted;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Hand card that swaps the positions of two pieces.
 *
 * <p>The player has to select exactly two distinct pieces (via {@link #setTarget(Action)})
 * before the card can be applied. Only pieces on unsafe tiles can be switched, so nobody
 * can be teleported out of a home slot, into a staircase or onto the finish.</p>
 */
public class SwitchCard implements ApplicableCard, Targeted<TargetPiece> {

    /**
     * Number of pieces that have to be selected before the card can be applied.
     */
    private static final int REQUIRED_TARGETS = 2;

    /**
     * The pieces selected so far, in the order of selection.
     */
    private final List<TargetPiece> selectedPieces = new ArrayList<>();

    /**
     * Swaps the tiles of the two selected pieces.
     *
     * <p>Both pieces are looked up and validated before anything is moved, so the game
     * stays unchanged if the action is invalid.</p>
     *
     * @param game   the game in which the card is played
     * @param player the player who plays the card
     * @throws InvalidActionException if fewer than two pieces were selected, a selected piece
     *                                does not exist or one of them stands on a safe tile
     */
    @Override
    public void apply(Game game, Player player) {
        if (requiresFurtherTarget(game, player)) {
            throw new InvalidActionException("two pieces must be selected before the card can be applied");
        }

        Piece first = findSwitchablePiece(game, selectedPieces.get(0));
        Piece second = findSwitchablePiece(game, selectedPieces.get(1));

        swapTiles(first, second);
    }

    /**
     * Returns the type of this card.
     *
     * @return {@link CardType#SWITCH}
     */
    @Override
    public CardType type() {
        return CardType.SWITCH;
    }

    /**
     * Returns whether the player still has to select a piece before the card can be applied.
     *
     * @param game   the game in which the card is played
     * @param player the player who plays the card
     * @return {@code true} if fewer than two pieces have been selected so far
     */
    @Override
    public boolean requiresFurtherTarget(Game game, Player player) {
        return selectedPieces.size() < REQUIRED_TARGETS;
    }

    /**
     * Returns all pieces that can still be selected: every switchable piece of every player,
     * except the ones that have already been selected.
     *
     * @param game   the game in which the card is played
     * @param player the player who plays the card
     * @return an unmodifiable list of the selectable pieces
     */
    @Override
    public List<TargetPiece> getPossibleTargets(Game game, Player player) {
        return game.getPlayers().stream()
                .flatMap(owner -> owner.getPieces().stream()
                        .filter(SwitchCard::isSwitchable)
                        .map(piece -> new TargetPiece(owner.getColor(), piece.getCurrentTile().getId())))
                .filter(target -> !selectedPieces.contains(target))
                .toList();
    }

    /**
     * Selects a piece for the switch.
     *
     * <p>Whether the piece exists and can be switched is checked when the card is applied.</p>
     *
     * @param target the piece to select, must be a {@link TargetPiece}
     * @throws InvalidActionException if the target is not a {@link TargetPiece}, two pieces have
     *                                already been selected or the piece was already selected
     */
    @Override
    public void setTarget(Action target) {
        if (!(target instanceof TargetPiece targetPiece)) {
            throw new InvalidActionException("target must be a piece");
        }
        if (selectedPieces.size() >= REQUIRED_TARGETS) {
            throw new InvalidActionException("enough targets have been selected");
        }
        if (selectedPieces.contains(targetPiece)) {
            throw new InvalidActionException("the same piece cannot be selected twice");
        }
        selectedPieces.add(targetPiece);
    }

    /**
     * Looks up the piece described by the target and checks that it can be switched.
     *
     * @param game   the game the piece belongs to
     * @param target the selected piece
     * @return the piece on the target tile
     * @throws InvalidActionException if the player has no piece on that tile
     *                                or the piece stands on a safe tile
     */
    private static Piece findSwitchablePiece(Game game, TargetPiece target) {
        Piece piece = game.getPlayer(target.targetPlayer())
                .getPiece(target.targetTile())
                .orElseThrow(() -> new InvalidActionException(
                        "no piece of " + target.targetPlayer() + " on tile " + target.targetTile()));

        if (!isSwitchable(piece)) {
            throw new InvalidActionException(
                    "piece on tile " + target.targetTile() + " is on a safe tile and cannot be switched");
        }
        return piece;
    }

    /**
     * Returns whether the piece may be switched. Pieces on safe tiles (home, staircase,
     * finish, safe spaces) are excluded.
     *
     * @param piece the piece to check
     * @return {@code true} if the piece stands on an unsafe tile
     */
    private static boolean isSwitchable(Piece piece) {
        return !piece.getCurrentTile().isSafe();
    }

    /**
     * Exchanges the current tiles of the two pieces.
     *
     * @param first  the first piece
     * @param second the second piece
     */
    private static void swapTiles(Piece first, Piece second) {
        Tile firstTile = first.getCurrentTile();
        Tile secondTile = second.getCurrentTile();

        first.moveTo(secondTile);
        second.moveTo(firstTile);
    }

}
