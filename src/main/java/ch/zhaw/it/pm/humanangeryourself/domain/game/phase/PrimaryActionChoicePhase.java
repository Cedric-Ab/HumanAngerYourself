package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.Action;
import ch.zhaw.it.pm.humanangeryourself.api.DecisionInfo;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

public class PrimaryActionChoicePhase extends GamePhase {
    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game the game instance this phase belongs to
     */
    public PrimaryActionChoicePhase(@NotNull Game game) {
        super(game);
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
