package ch.zhaw.it.pm.humanangeryourself.ui.viewmodel;

import ch.zhaw.it.pm.humanangeryourself.api.GameListener;
import ch.zhaw.it.pm.humanangeryourself.api.GameService;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.InvalidActionException;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Objects;

/**
 * View model between the UI and the {@link GameService}.
 * <p>
 * It turns user input into actions that are sent to the service and exposes the
 * latest
 * {@link GameSnapshot} as a JavaFX property the UI can bind to. It only knows
 * the
 * {@link GameService} interface and never accesses domain objects.
 * </p>
 */
public class GameViewModel implements GameListener {
    /** Service that receives the actions and provides the game state. */
    private final GameService service;

    /** The latest snapshot of the game state. */
    private final ObjectProperty<GameSnapshot> snapshot = new SimpleObjectProperty<>();
    private final ObjectProperty<String> message = new SimpleObjectProperty<>("");

    /**
     * Creates a view model for the given game service.
     *
     * @param service the game service to send actions to and receive snapshots from
     */
    public GameViewModel(GameService service) {
        this.service = Objects.requireNonNull(service, "service");

        snapshot.set(service.getSnapshot());
        service.addListener(this);
    }

    /**
     * Replaces the stored snapshot with the new game state.
     *
     * @param snapshot the new snapshot of the game
     */
    @Override
    public void onGameChanged(GameSnapshot snapshot) {
        this.snapshot.set(snapshot);
    }

    public void roll() {
        service.submit(new Roll());
    }
}
