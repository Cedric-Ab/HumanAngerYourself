package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.action.TargetPiece;

/**
 * Interface for cards that require a target to be played.
 */
public sealed interface Targeted extends Card {
    /**
     * Interface for cards that require two piece targets to be played.
     */
    non-sealed interface DualPieceTargeted extends Targeted {
        /**
         * Sets the first target of the card.
         *
         * @param target the first target piece
         */
        void setFirstTarget(TargetPiece target);

        /**
         * Sets the second target of the card.
         *
         * @param target the second target piece
         */
        void setSecondTarget(TargetPiece target);
    }
}
