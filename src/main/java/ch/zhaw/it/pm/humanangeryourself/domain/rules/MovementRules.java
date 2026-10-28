package ch.zhaw.it.pm.humanangeryourself.domain.rules;

import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import java.util.List;
import java.util.Optional;

public class MovementRules {

    public boolean legalMove(){return false;}
    private boolean canEnterFinish(){return false;}
    private boolean canLeaveHome(int rollResult){return false;}
    public Optional<Piece> pieceToSendBackHome(Piece pieceMoved, Board board, List<Player> players){return Optional.of(null);}

}
