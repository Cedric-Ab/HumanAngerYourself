package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

public class GamblingPhase extends GamePhaseBase {
    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game the game instance this phase belongs to
     */
    public GamblingPhase(@NotNull Game game) {
        super(game);
    }

    @Override
    public @NotNull GamePhase process() {
        // TODO: update when gambling can yield action cards
        int result = getGame().getGamblingRules().spin();
        getCurrentPlayer().addMoney(result);

        return new EndOfTurnPhase(getGame());
    }

    @Override
    public @NotNull GamePhase handle(Player player, Action action) {
        throw new IllegalCallerException("GamblingPhase cannot handle player input");
    }
}
