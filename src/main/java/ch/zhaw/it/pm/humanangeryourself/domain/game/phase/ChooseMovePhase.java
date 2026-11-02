package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.MovePiece;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.DecisionInfo;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChooseMovePhase extends GamePhase {
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
        int roll = 6; // TODO: randomize

        Player player = getGame().getTurn().getPlayer();

        // TODO: add getPossibleMoves()
//         List<MovePiece> possibleMoves = getGame().getMovementRules().getPossibleMoves(player, roll);
        List<MovePiece> possibleMoves = new ArrayList<>();

        setDecisionInfo(new DecisionInfo(player, possibleMoves));

        return this;
    }

    @Override
    public @NotNull GamePhase handle(Player player, Action action) {
        if (getGame().getTurn().getPlayer().equals(player)) {
            throw new IllegalArgumentException("Only the current player can perform actions in this phase.");
        }
        if (isIllegalAction(action) || !(action instanceof MovePiece(int startingTile, int targetTile))) {
            throw new IllegalArgumentException("Action is not available in this phase.");
        }

        // TODO: add getPiece()
//         Piece piece = player.getPiece(startingTile);
        Piece piece = null;

        piece.moveTo(getGame().getBoard().getTileById(targetTile));

        // TODO: add consequences

        return new EndOfTurnPhase(getGame());
    }
}
