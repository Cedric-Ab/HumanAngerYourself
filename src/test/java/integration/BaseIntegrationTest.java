package integration;

import ch.zhaw.it.pm.humanangeryourself.api.action.EndTurn;

import ch.zhaw.it.pm.humanangeryourself.api.action.Roll;

import org.junit.jupiter.api.BeforeEach;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.Gamble;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.DecisionInfo;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.game.GameBuilder;
import ch.zhaw.it.pm.humanangeryourself.domain.game.GameController;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.GamblingRules;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.MovementRules;
import ch.zhaw.it.pm.humanangeryourself.service.SnapshotFactory;

import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

abstract class BaseIntegrationTest {

    protected GameController controller;
    private MovementRules movementRules;
    private GamblingRules gamblingRules;

    @BeforeEach
    void setUp() {
        movementRules = spy(MovementRules.class);
        gamblingRules = spy(GamblingRules.class);

        Game game = new GameBuilder()
                .setMovementRules(movementRules)
                .setGamblingRules(gamblingRules)
                .createGame();

        controller = new GameController(game);
    }

    protected GameSnapshot getSnapshot() {
        return SnapshotFactory.create(controller, PlayerColor.RED);
    }

    protected DecisionInfo getDecision() {
        return controller.getDecision();
    }

    protected PlayerColor decider() {
        return getDecision().decider();
    }

    protected List<? extends Action> availableActions() {
        return getDecision().availableActions();
    }

    /**
     * Sets the result of the die roll to a specific value for testing purposes.
     *
     * @param result the desired result of the die roll,
     *               should be between 1 and 6 inclusive according to {@link MovementRules#rollDie()}
     */
    protected void setRollDieResult(int result) {
        doReturn(result)
                .when(movementRules)
                .rollDie();
    }

    protected void setGambleResult(int result) {
        doReturn(result)
                .when(gamblingRules)
                .spin();
    }

    /**
     * Submits an action to the game controller if it is valid in the current decision context.
     * <p>
     * Use when the given action is expected to be valid.
     * If the action is not valid, an IllegalStateException, stating that the test is outdated, will be thrown.
     *
     * @param action the action to submit
     */
    protected void submitValid(Action action) {
        if (!availableActions().contains(action)) {
            throw new IllegalStateException("Test is outdated. Current decision does not allow action: " + action);
        }

        controller.submit(decider(), action);
    }

    /**
     * Rushes through a turn by setting the gamble result to 0 and submitting a Gamble and EndTurn action.
     * <p>
     * This method is used to quickly progress through a turn in the game for testing purposes.
     * Use this method when you want to skip the details of a turn and move on to the next player's turn.
     */
    protected void rushThroughTurn() {
        setGambleResult(0);
        submitValid(new Gamble());
        submitValid(new EndTurn());
    }

    protected void rollNumber(int result) {
        setRollDieResult(result);
        submitValid(new Roll());
    }
}
