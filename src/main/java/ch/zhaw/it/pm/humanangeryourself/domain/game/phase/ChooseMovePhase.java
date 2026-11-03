package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.MovePiece;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.DecisionInfo;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChooseMovePhase extends GamePhaseBase {
    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game the game instance this phase belongs to
     */
    public ChooseMovePhase(@NotNull Game game) {
        super(game);
    }

    @Override
    public @NotNull GamePhase process() {
        int roll = getGame().getGamblingRules().rollDie();

        Player player = getGame().getTurn().getPlayer();

        List<MovePiece> possibleMoves = getGame().getMovementRules().getPossibleMoves(player, roll);

        setDecisionInfo(new DecisionInfo(player, possibleMoves));

        return this;
    }

    @Override
    public @NotNull GamePhase handle(Player player, Action action) {
        if (getGame().getTurn().getPlayer().equals(player)) {
            throw new IllegalArgumentException("Only the current player can perform actions in this phase.");
        }
        if (isIllegalAction(action) || !(action instanceof MovePiece(int startTileID, int targetTileID))) {
            throw new IllegalArgumentException("Action is not available in this phase.");
        }

        Tile startTile = getGame().getBoard().getTileById(startTileID);
        Tile endTile = getGame().getBoard().getTileById(targetTileID);

        Piece piece = player.getPiece(startTile);

        piece.moveTo(endTile);

        List<Piece> piecesToSendBackHome = getGame().getMovementRules().piecesToSendBackHome(piece);
        piecesToSendBackHome.forEach(Piece::sendHome);

        return new EndOfTurnPhase(getGame());
    }
}
