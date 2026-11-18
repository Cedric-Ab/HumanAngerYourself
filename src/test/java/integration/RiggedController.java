package integration;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.DecisionInfo;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.game.GameBuilder;
import ch.zhaw.it.pm.humanangeryourself.domain.game.GameController;
import ch.zhaw.it.pm.humanangeryourself.domain.game.GameObserver;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.GamblingRules;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.MovementRules;
import ch.zhaw.it.pm.humanangeryourself.service.SnapshotFactory;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 *
 */
public class RiggedController extends GameController {
    private final GameController controller;

    private final MovementRules movementRules;
    private final GamblingRules gamblingRules;

    public RiggedController() {
        movementRules = spy(MovementRules.class);
        gamblingRules = spy(GamblingRules.class);

        Game game = new GameBuilder()
                .setMovementRules(movementRules)
                .setGamblingRules(gamblingRules)
                .createGame();

        controller = new GameController(game);
    }

    @Override
    public void addObserver(GameObserver observer) {
        throw new UnsupportedOperationException("Adding observers is not supported");
    }

    @Override
    public void submit(PlayerColor color, Action action) {
        controller.submit(color, action);
    }

    @Override
    public DecisionInfo getDecision() {
        return controller.getDecision();
    }

    /**
     * Returns the game instance managed by this controller.
     * <p>
     * Should only be used to arrange the game state for testing purposes.
     * Do not use to inspect the game state, use {@link #getSnapshot()} instead,
     * as else we're no longer testing the public interface of the game controller.
     *
     * @return the game instance managed by this controller
     */
    @Override
    public Game getGame() {
        return controller.getGame();
    }

    public GameSnapshot getSnapshot() {
        return SnapshotFactory.create(controller, PlayerColor.RED);
    }

    /**
     * Sets the result of the die roll to a specific value for testing purposes.
     *
     * @param result the desired result of the die roll,
     *               should be between 1 and 6 inclusive according to {@link MovementRules#rollDie()}
     */
    public void setRollDieResult(int result) {
        doReturn(result)
                .when(movementRules)
                .rollDie();
    }

    public void setGambleResult(int result) {
        doReturn(result)
                .when(gamblingRules)
                .spin();
    }
}
