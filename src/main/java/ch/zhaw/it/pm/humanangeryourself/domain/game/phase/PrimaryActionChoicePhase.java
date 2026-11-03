package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.Gamble;
import ch.zhaw.it.pm.humanangeryourself.api.action.Roll;
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
        setDecisionInfo(getCurrentPlayer(), List.of(new Roll(), new Gamble()));
        return this;
    }

    @Override
    public @NotNull GamePhase handle(Player player, Action action) {
        validatePlayer(player);
        validateAction(action);

        if (action instanceof Roll) {
            return new ChooseMovePhase(getGame());
        } else if (action instanceof Gamble) {
            return new GamblingPhase(getGame());
        } else {
            throw new RuntimeException("Unexpected action type: " + action.getClass().getName());
        }
    }
}
