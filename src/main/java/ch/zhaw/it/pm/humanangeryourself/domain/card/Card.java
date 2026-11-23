package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;

/**
 * Base abstraction for all cards in the game.
 */
public interface Card {
    static Card fromType(CardType type) {
        return switch (type) {
            case DOUBLE_IT -> new DoubleItCard();
            case TRY_AGAIN -> new TryAgainCard();
            case MONEY_TIGER -> new MoneyTigerCard();
            case ANGER_REVERSE -> new AngerReverseCard();
            case SPEED_BOOST -> new SpeedBoostCard();
            case SWITCH -> new SwitchCard();
            case JACKPOT -> new JackpotCard();
            case CHARITY -> new CharityCard();
        };
    }

    /**
     * Returns the type of this card.
     *
     * @return the {@link CardType} of this card
     */
    public CardType type();
}
