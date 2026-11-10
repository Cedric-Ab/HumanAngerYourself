package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.MovePiece;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Game phase in which the current player rolls the die and chooses which piece to move.
 * After the chosen move is executed, pieces that were displaced are sent back home
 * and the game continues with the {@link EndOfTurnPhase}.
 */
public class ChooseMovePhase extends GamePhaseBase {
    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game the game instance this phase belongs to
     */
    public ChooseMovePhase(@NotNull Game game) {
        super(game);
    }

    /**
     * Rolls the die for the current player and determines all moves that are possible with the
     * rolled number. The possible moves are published as the decision the current player has to make.
     *
     * @return this phase, as it waits for the player's decision
     */
    @Override
    public @NotNull GamePhase process() {
        int roll = getGame().getMovementRules().rollDie();

        List<MovePiece> possibleMoves = getGame().getMovementRules().getPossibleMoves(getCurrentPlayer(), roll);
        setDecisionInfo(getCurrentPlayer(), possibleMoves);

        return this;
    }

    /**
     * Executes the move chosen by the player. The piece is moved to the target tile and all pieces
     * that have to go back home because of this move (for example, pieces that were hit) are sent home.
     *
     * @param player the player who made the decision
     * @param action the chosen action, must be a {@link MovePiece}
     * @return the {@link EndOfTurnPhase} that follows after the move
     * @throws IllegalArgumentException if the action is not a {@link MovePiece}
     * @throws java.util.NoSuchElementException if the player has no piece on the start tile
     */
    @Override
    public @NotNull GamePhase handle(Player player, Action action) {
        validatePlayer(player);
        validateAction(action);

        if (!(action instanceof MovePiece(int startTileID, int targetTileID))) {
            throw new IllegalArgumentException("Action is not available in this phase.");
        }

        Tile startTile = getGame().getBoard().getTileById(startTileID);
        Tile endTile = getGame().getBoard().getTileById(targetTileID);

        Piece piece = player.getPiece(startTile).orElseThrow();

        piece.moveTo(endTile);

        List<Piece> piecesToSendBackHome = getGame().getMovementRules().piecesToSendBackHome(piece);
        piecesToSendBackHome.forEach(Piece::sendHome);

        return new EndOfTurnPhase(getGame());
    }
}
