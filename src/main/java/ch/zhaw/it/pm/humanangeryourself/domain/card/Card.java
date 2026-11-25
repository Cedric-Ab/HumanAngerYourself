package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.domain.card.types.AngerReverseCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.types.CharityCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.types.DoubleItCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.types.JackpotCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.types.MoneyTigerCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.types.SpeedBoostCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.types.SwitchCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.types.TryAgainCard;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

/**
 * Represents a card in the game.
 * <p>
 * Each card has a specific type and can apply its effect to a player in the game.
 */
public interface Card {
    /**
     * Constructs a {@code Card} object from the given {@link CardType}.
     * <p>
     * This is the inverse lookup of {@link Card#type()}, i.e. {@code someType == Card.fromType(someType).type()} should always be true.
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

    /**
     * Returns the type of this card.
     *
     * @return the {@link CardType} of this card
     */
    CardType type();

    /**
     * Applies the effect of this card.
     *
     * @param game   the game on which the card is applied
     * @param player the player on which the card is applied
     */
    void apply(Game game, Player player);
}
