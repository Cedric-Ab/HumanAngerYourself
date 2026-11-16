package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.Gamble;
import ch.zhaw.it.pm.humanangeryourself.api.action.Roll;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Game phase at the start of a turn, in which the current player chooses between rolling the die
 * and gambling. Rolling continues with the {@link ChooseMovePhase}, gambling with the
 * {@link GamblingPhase}.
 */
public class PrimaryActionChoicePhase extends GamePhaseBase {
    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game the game instance this phase belongs to
     */
    public PrimaryActionChoicePhase(@NotNull Game game) {
        super(game);
    }

    /**
     * Offers the current player the choice between {@link Roll} and {@link Gamble}.
     *
     * @return this phase, as it waits for the player's decision
     */
    @Override
    public @NotNull GamePhase process() {
        setDecisionInfo(getCurrentPlayer(), List.of(new Roll(), new Gamble()));
        return this;
    }

    /**
     * Handles the player's choice and moves on to the matching phase.
     *
     * @param player the player who made the decision
     * @param action the chosen action, either {@link Roll} or {@link Gamble}
     * @return a {@link ChooseMovePhase} after a roll, or a {@link GamblingPhase} after a gamble
     * @throws RuntimeException if the action is neither {@link Roll} nor {@link Gamble}
     */
    @Override
    public @NotNull GamePhase handle(Player player, Action action) {
        validatePlayer(player);
        validateAction(action);

        return switch (action) {
            case Roll ignored -> new ChooseMovePhase(getGame());
            case Gamble ignored -> new GamblingPhase(getGame());
            default -> throw new RuntimeException("Unexpected action type: " + action.getClass().getName());
        };
    }
}
