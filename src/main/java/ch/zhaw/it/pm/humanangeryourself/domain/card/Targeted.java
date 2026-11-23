package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.action.TargetPiece;

public sealed interface Targeted extends Card {
    non-sealed interface DualTargeted extends Targeted {
        void setFirstTarget(TargetPiece target);

        void setSecondTarget(TargetPiece target);
    }
}
