package integration;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.DecisionInfo;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PrimaryActionResult;
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
 * A rigged game controller that allows for controlling the results of die rolls and gambling spins.
 */
public class RiggedController extends GameController {
    private final MovementRules movementRules;
    private final GamblingRules gamblingRules;

    private RiggedController(Game game) {
        super(game);

        movementRules = game.getMovementRules();
        gamblingRules = game.getGamblingRules();
    }

    public RiggedController() {
        this(createGame());
    }

    private static Game createGame() {
        return new GameBuilder()
                .setMovementRules(spy(MovementRules.class))
                .setGamblingRules(spy(GamblingRules.class))
                .createGame();
    }

    @Override
    public void addObserver(GameObserver observer) {
        throw new UnsupportedOperationException("Adding observers is not supported");
    }

    @Override
    public void submit(PlayerColor color, Action action) {
        super.submit(color, action);
    }

    @Override
    public DecisionInfo getDecision() {
        return super.getDecision();
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
        return super.getGame();
    }

    public GameSnapshot getSnapshot(PlayerColor viewer) {
        return SnapshotFactory.create(this, viewer);
    }

    public GameSnapshot getSnapshot() {
        return getSnapshot(PlayerColor.RED);
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

    public void setGambleResult(PrimaryActionResult.GambleResult result) {
        doReturn(result)
                .when(gamblingRules)
                .spin();
    }

    public void setGambleResult(int money) {
        PrimaryActionResult.GambleResult result = new PrimaryActionResult.GambleResult.MoneyResult(money);
        setGambleResult(result);

        doReturn(result)
                .when(gamblingRules)
                .spinWin();
    }

    public void setGambleResult(CardType cardType) {
        PrimaryActionResult.GambleResult result = new PrimaryActionResult.GambleResult.CardResult(cardType);
        setGambleResult(result);

        doReturn(result)
                .when(gamblingRules)
                .spinCard();
    }
}
