package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

public class StartOfTurnPhase extends GamePhaseBase {
    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game the game instance this phase belongs to
     */
    public StartOfTurnPhase(@NotNull Game game) {
        super(game);
    }

    public @NotNull GamePhase process() { return this; }

    @Override
    public @NotNull GamePhase handle(Player player, Action action) {
        return null;
    }
}
