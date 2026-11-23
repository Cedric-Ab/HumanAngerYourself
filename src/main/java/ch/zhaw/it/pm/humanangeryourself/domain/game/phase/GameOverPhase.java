package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.InvalidActionException;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents the game phase when the game is over. This phase is reached when a player has won the game.
 */
public class GameOverPhase extends GamePhaseBase {
    private final Player winner;

    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game the game instance this phase belongs to
     */
    public GameOverPhase(@NotNull Game game, @NotNull Player winner) {
        super(game);
        this.winner = winner;
    }

    @Override
    public @NotNull GamePhase process() {
        getGame().setAsWinner(winner);

        setDecisionInfo(winner, List.of());
        return this;
    }

    @Override
    public @NotNull GamePhase handle(Player player, Action action) {
        throw new InvalidActionException(player, action, "Game is over, no further actions are allowed.");
    }
}
