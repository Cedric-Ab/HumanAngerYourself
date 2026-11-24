package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.Roll;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Game Phase where a player rolls again. Always continues with the choose move phase (where the actual rolling happens).
 * <p>
 * The purpose of this phase is to force the player to press the button "Roll" again,
 * rather than automatically rolling the die again, because that would be unintuitive.
 * </p>
 */
public class RollAgainPhase extends GamePhaseBase {
    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game the game instance this phase belongs to
     */
    public RollAgainPhase(@NotNull Game game) {
        super(game);
    }

    /**
     * Offers the player the option to roll the die.
     *
     * @return this phase, as it waits for the player's decision
     */
    @Override
    public @NotNull GamePhase process() {
        setDecisionInfo(getCurrentPlayer(), List.of(new Roll()));
        return this;
    }

    /**
     * Validates the input and moves on to the {@link ChooseMovePhase}.
     *
     * @param player the player making the action
     * @param action the action taken by the player
     * @return the next phase
     */
    @Override
    public @NotNull GamePhase handle(Player player, Action action) {
        validatePlayer(player);
        validateAction(action);

        return new ChooseMovePhase(getGame());
    }
}
