package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.config.CardConfig;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

/**
 * Instant card that awards the configured amount of money to the player who drew it.
 */
public class JackpotCard implements InstantCard {

    /**
     * The configuration providing the jackpot amount.
     */
    private final CardConfig config;

    /**
     * Creates a jackpot card using the default configuration.
     */
    public JackpotCard() {
        this(CardConfig.defaults());
    }

    /**
     * Creates a jackpot card using the given configuration.
     *
     * @param config the configuration providing the jackpot amount
     */
    public JackpotCard(CardConfig config) {
        this.config = config;
    }

    /**
     * Adds the configured jackpot amount to the player's balance.
     *
     * @param game   the game in which the card is drawn
     * @param player the player whose balance is increased
     */
    @Override
    public void apply(Game game, Player player) {
        player.changeBalanceBy(config.jackpot());
    }
}
