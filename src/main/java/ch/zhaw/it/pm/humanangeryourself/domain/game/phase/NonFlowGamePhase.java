package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a game phase that transitions to a specified next phase after completion.
 * <p>
 * The game phase does not know or decide what the next phase is,
 * but it is provided with a reference to the next phase during construction.
 * This allows for flexible game flow management.
 */
public abstract class NonFlowGamePhase extends GamePhase {

    private final @NotNull GamePhase nextPhase;

    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game      the game instance this phase belongs to
     * @param nextPhase the next game phase to transition to after this phase is completed
     */
    public NonFlowGamePhase(@NotNull Game game, @NotNull GamePhase nextPhase) {
        super(game);
        this.nextPhase = nextPhase;
    }

    /**
     * Returns the next game phase to transition to after this phase is completed.
     *
     * @return the next game phase
     */
    public @NotNull GamePhase getNextPhase() {
        return nextPhase;
    }
}
