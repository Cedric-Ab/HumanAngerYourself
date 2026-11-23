package ch.zhaw.it.pm.humanangeryourself.api.snapshot;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;

/**
 * Represents the result of a primary action.
 */
public sealed interface PrimaryActionResult {
    /**
     * Represents the result of a die roll.
     *
     * @param dieRoll the die roll, must be greater than 0
     */
    record RollResult(int dieRoll) implements PrimaryActionResult {
        public RollResult {
            if (dieRoll < 1) {
                throw new IllegalArgumentException("Die roll must be greater than 0, but was " + dieRoll);
            }
        }
    }

    /**
     * Represents the result of gambling.
     */
    sealed interface GambleResult extends PrimaryActionResult {
        /**
         * Represents an outcome where gambling yielded money.
         *
         * @param amount the amount of money (may be negative)
         */
        record MoneyResult(long amount) implements GambleResult { }

        /**
         * Represents an outcome where gambling yielded a card.
         *
         * @param cardType the card type (may not be null)
         */
        record CardResult(CardType cardType) implements GambleResult { }
    }
}
