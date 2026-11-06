package ch.zhaw.it.pm.humanangeryourself.domain.game;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.DecisionInfo;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.domain.game.phase.GamePhase;
import ch.zhaw.it.pm.humanangeryourself.domain.game.phase.StartOfTurnPhase;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The public interface of the game controller.
 * <p>
 * The game controller is responsible for controlling the game flow and managing the game state.
 * It provides methods for submitting actions, retrieving decisions, and accessing the game instance.
 */
public class GameController {

    private final Game game;

    private GamePhase currentPhase;

    private final List<GameObserver> observers = new ArrayList<>();

    /**
     * Creates a new instance of the GameController with the specified game instance.
     *
     * @param game the game instance to be controlled by this controller
     */
    public GameController(Game game) {
        this.game = game;
        startGame();
    }

    /**
     * Creates a new instance of the GameController with a new Game instance.
     */
    public GameController() {
        this(new Game());
    }

    /**
     * Registers an observer that is notified after every state change.
     *
     * @param observer the observer to register
     */
    public void addObserver(GameObserver observer) {
        observers.add(Objects.requireNonNull(observer, "observer"));
    }

    /**
     * Notifies all registered observers that the game state has changed.
     * <p>
     * Called once per submitted action, after all phase transitions have been processed.
     */
    private void notifyObservers() {
        for (GameObserver observer : List.copyOf(observers)) {
            observer.onGameChanged();
        }
    }

    /**
     * Submits an action for the specified player.
     *
     * @param color  the color of the player submitting the action
     * @param action the action being submitted
     */
    public void submit(PlayerColor color, Action action) {
        Player player = game.getPlayer(color);
        GamePhase nextPhase = currentPhase.handle(player, action);
        setPhaseTo(nextPhase);
        notifyObservers();
    }

    /**
     * Returns the current decision information for the game.
     *
     * @return the current decision information
     */
    public DecisionInfo getDecision() {
        // TODO implement correctly
        return new DecisionInfo(PlayerColor.BLUE, new ArrayList<>());
        // return currentPhase.getDecision();
    }

    /**
     * Returns the game instance that is controlled by this controller.
     *
     * @return the game instance
     */
    public Game getGame() {
        return game;
    }

    private void startGame() {
        setPhaseTo(new StartOfTurnPhase(game));
    }

    /**
     * Sets the current phase of the game to the specified phase and processes it.
     * <p>
     * Ensures {@link GamePhase#process()} is called for each phase.
     *
     * @param phase the new phase to set as the current phase, may be the same as the current phase
     */
    private void setPhaseTo(GamePhase phase) {
        if (phase == currentPhase) {
            return;
        }

        currentPhase = phase;

        GamePhase nextPhase = currentPhase.process();
        setPhaseTo(nextPhase);
    }
}
