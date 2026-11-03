package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.Gamble;
import ch.zhaw.it.pm.humanangeryourself.api.action.Roll;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.DecisionInfo;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PrimaryActionChoicePhase extends GamePhaseBase {
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
        setDecisionInfo(getGame().getTurn().getPlayer(), List.of(new Roll(), new Gamble()));
        return this;
    }

    @Override
    public @NotNull GamePhase handle(Player player, Action action) {
        if (getGame().getTurn().getPlayer().equals(player)) {
            throw new IllegalArgumentException("Only the current player can perform actions in this phase.");
        }
        if (isIllegalAction(action)) {
            throw new IllegalArgumentException("Action is not available in this phase.");
        }
        if (action instanceof Roll) {
            return new ChooseMovePhase(getGame());
        } else if (action instanceof Gamble) {
            return new GamblingPhase(getGame());
        } else {
            throw new IllegalArgumentException("Action is not available in this phase.");
        }
    }
}
