package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.api.action.MovePiece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import java.util.List;
import java.util.Optional;

public class MovementRules {

    public boolean legalMove() {
        return false;
    }

    private boolean canEnterFinish() {
        return false;
    }

    public List<MovePiece> getPossibleMoves(Player player, int roll) {
        // TODO: high priority method!!!
        return List.of();
    }

    public List<Piece> piecesToSendBackHome(Piece pieceMoved, Player player) {
        // low priority
        return List.of();
    }
}
