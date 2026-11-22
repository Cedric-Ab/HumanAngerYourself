package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;

import java.util.Objects;

/**
 * The result of a gambling spin. A result is either a money amount (a win if positive,
 * a loss if negative) or a card, never both.
 *
 * @see GamblingRules
 */
public class SpinResult {

    /** The money amount of this result, or {@code null} if this result is a card. */
    private final Long amount;

    /** The card of this result, or {@code null} if this result is a money amount. */
    private final CardType cardType;

    /**
     * Creates a result that represents a money amount.
     *
     * @param amount the amount won (positive) or lost (negative)
     * @throws NullPointerException if {@code amount} is {@code null}
     */
    public SpinResult(Long amount) {
        this.amount = Objects.requireNonNull(amount, "amount must not be null");
        this.cardType = null;
    }

    /**
     * Creates a result that represents a card.
     *
     * @param cardType the type of the card that was won
     * @throws NullPointerException if {@code cardType} is {@code null}
     */
    public SpinResult(CardType cardType) {
        this.amount = null;
        this.cardType = Objects.requireNonNull(cardType, "cardType must not be null");
    }

    /**
     * Checks whether this result is a card rather than a money amount.
     *
     * @return {@code true} if this result is a card, {@code false} if it is a money amount
     */
    public boolean isCard(){
        return cardType != null;
    }

    /**
     * Returns the money amount of this result.
     *
     * @return the amount (positive for a win, negative for a loss), or {@code null}
     *         if this result is a card
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * Returns the card of this result.
     *
     * @return the card type, or {@code null} if this result is a money amount
     */
    public CardType getCardType() {
        return cardType;
    }
}
