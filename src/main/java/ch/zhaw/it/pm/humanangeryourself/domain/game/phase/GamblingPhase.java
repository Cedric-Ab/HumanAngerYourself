package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult.GambleResult;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

/**
 * Game phase in which the current player gambles. Depending on the gambling settings, the player
 * wins or loses money or receives a card. Not implemented yet.
 */
public class GamblingPhase extends GamePhaseBase {
    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game the game instance this phase belongs to
     */
    public GamblingPhase(@NotNull Game game) {
        super(game);
    }

    /**
     * Carries out the gamble for the current player. Not implemented yet.
     *
     * @return the phase to continue with
     */
    @Override
    public @NotNull GamePhase process() {
        GambleResult result = getGame().getGamblingRules().spin();

        getGame().getTurn().setPrimaryActionResult(result);

        return switch (result) {
            case GambleResult.MoneyResult m -> {
                getCurrentPlayer().changeBalanceBy(m.amount());
                yield createEndOfTurnPhase();
            }
            case GambleResult.CardResult c -> applyCardResult(c);
        };
    }

    private @NotNull EndOfTurnPhase createEndOfTurnPhase() {
        return new EndOfTurnPhase(getGame());
    }

    private GamePhase applyCardResult(GambleResult.CardResult cardResult) {
        if (cardResult.cardType().isInstant()) {
            // TODO: activate card effect
            return createEndOfTurnPhase();
        } else {
            getCurrentPlayer().addCard(Game.cardFromType(cardResult.cardType()));
            return new DiscardPhase(getGame(), createEndOfTurnPhase(), getCurrentPlayer());
        }
    }

    /**
     * Handles a decision of the player during gambling.
     *
     * @param player the player who made the decision
     * @param action the chosen action
     * @return the phase to continue with
     * @throws IllegalCallerException if this method is called, as the GamblingPhase does not handle player input
     */
    @Override
    public @NotNull GamePhase handle(Player player, Action action) {
        throw new IllegalCallerException("GamblingPhase cannot handle player input");
    }
}
