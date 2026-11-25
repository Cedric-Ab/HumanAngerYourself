package ch.zhaw.it.pm.humanangeryourself.domain.card.types;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.config.CardConfig;
import ch.zhaw.it.pm.humanangeryourself.domain.card.Card;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

/**
 * Instant card that deducts the configured amount of money from the player who drew it.
 */
public class CharityCard implements Card {

    /** The configuration providing the charity amount. */
    private final CardConfig config;

    /**
     * Creates a charity card using the default configuration.
     */
    public CharityCard() {
        this(CardConfig.defaults());
    }

    /**
     * Creates a charity card using the given configuration.
     *
     * @param config the configuration providing the charity amount
     */
    public CharityCard(CardConfig config) {
        this.config = config;
    }

    /**
     * Deducts the configured charity amount from the player's balance.
     *
     * @param game   the game in which the card is drawn
     * @param player the player whose balance is reduced
     */
    @Override
    public void apply(Game game, Player player) {
        player.changeBalanceBy(config.charity());
    }

    @Override
    public CardType type() {
        return CardType.CHARITY;
    }
}
