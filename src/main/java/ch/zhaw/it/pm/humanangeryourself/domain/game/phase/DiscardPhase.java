package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.Action;
import ch.zhaw.it.pm.humanangeryourself.api.DecisionInfo;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

public class DiscardPhase extends NonFlowGamePhase {
    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game      the game instance this phase belongs to
     * @param nextPhase the next game phase to transition to after this phase is completed
     */
    public DiscardPhase(@NotNull Game game, @NotNull GamePhase nextPhase) {
        super(game, nextPhase);
    }

    @Override
    public @NotNull GamePhase process() {
        return null;
    }

    @Override
    public @NotNull DecisionInfo getDecision() {
        return null;
    }

    @Override
    public @NotNull GamePhase handle(Player player, Action action) {
        return null;
    }
}
