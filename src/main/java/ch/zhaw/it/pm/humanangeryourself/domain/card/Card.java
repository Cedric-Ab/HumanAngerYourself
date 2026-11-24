package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;

/**
 * Base abstraction for all cards in the game.
 */
public interface Card {
    /**
     * Constructs a {@code Card} object from the given {@link CardType}.
     * <p>
     * This is the inverse lookup of Card.type(), i.e. someType == Card.fromType(someType).type() should always be true.
     * </p>
     *
     * @param type the {@link CardType} of the card to create
     * @return a {@code Card} object representing the specified card type
     */
    static Card fromType(CardType type) {
        return switch (type) {
            case ANGER_REVERSE -> new AngerReverseCard();
            case CHARITY -> new CharityCard();
            case DOUBLE_IT -> new DoubleItCard();
            case JACKPOT -> new JackpotCard();
            case MONEY_TIGER -> new MoneyTigerCard();
            case SPEED_BOOST -> new SpeedBoostCard();
            case SWITCH -> new SwitchCard();
            case TRY_AGAIN -> new TryAgainCard();
        };
    }

    boolean preconditionsMet();
    /**
     * Returns the type of this card.
     *
     * @return the {@link CardType} of this card
     */
    CardType type();
}
