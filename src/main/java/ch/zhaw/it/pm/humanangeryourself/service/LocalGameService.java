package ch.zhaw.it.pm.humanangeryourself.service;

import ch.zhaw.it.pm.humanangeryourself.api.InvalidActionException;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.GameListener;
import ch.zhaw.it.pm.humanangeryourself.api.GameService;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;
import ch.zhaw.it.pm.humanangeryourself.domain.game.GameController;
import ch.zhaw.it.pm.humanangeryourself.domain.game.GameObserver;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Local (hotseat) implementation of {@link GameService}.
 * <p>
 * Acts as an adapter between the UI and the domain.
 * <p>
 * <ul>
 *     <li>the UI only knows the {@link GameService} interface</li>
 *     <li>this class translates its calls to the {@link GameController}</li>
 *     <li>in the other direction it observes the controller and forwards every
 *          change as a {@link GameSnapshot} to all registered {@link GameListener}</li>
 * </ul>
 * <p>
 */
public class LocalGameService implements GameService, GameObserver {

    private final GameController controller;
    private final List<GameListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * Creates a service for the given game and registers itself as observer.
     *
     * @param controller the game controller to adapt
     */
    public LocalGameService(GameController controller) {
        this.controller = Objects.requireNonNull(controller, "controller");
        controller.addObserver(this);
    }

    /**
     * Returns the current game state <b> as seen by the player who currently has to decide </b>.
     *
     * @return a new snapshot of the game
     */
    @Override
    public GameSnapshot getSnapshot() {
        return SnapshotFactory.create(controller, currentDecider());
    }

    /**
     * Submits an action on behalf of the player who currently has to decide.
     *
     * @param action the action chosen in the UI
     * @throws InvalidActionException if the game action is not valid
     */
    @Override
    public void submit(Action action) {
        Objects.requireNonNull(action, "action");
        PlayerColor decider = currentDecider();
        if (decider == null) { throw new InvalidActionException("Action requires a decider"); }

        controller.submit(decider, action);
    }

    /**
     * Registers a listener that is informed about every game change.
     *
     * @param listener the listener to add
     */
    @Override
    public void addListener(GameListener listener) {
        listeners.add(Objects.requireNonNull(listener, "listener"));
    }


    /**
     * Called by the {@link GameController} after every state change.
     * <p>
     * Translates the domain notification into the API notification: creates a new
     * {@link GameSnapshot} and forwards it to all registered {@link GameListener}s.
     */
    @Override
    public void onGameChanged() {
        GameSnapshot snapshot = getSnapshot();
        for (GameListener listener : listeners) {
            listener.onGameChanged(snapshot);
        }
    }


    /**
     * Returns the player who currently has to decide.
     * <p>
     * Usually this is the player whose turn it is, but it may be different during a reaction
     * (the affected player) or an auction (the bidding player)
     * reaction (the affected player) or an auction (the bidding player).
     *
     * @return the deciding player
     */
    private PlayerColor currentDecider() {
        return controller.getDecision().decider();
    }
}
