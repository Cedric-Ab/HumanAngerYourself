package ch.zhaw.it.pm.humanangeryourself.ui.viewmodel;

import ch.zhaw.it.pm.humanangeryourself.api.GameListener;
import ch.zhaw.it.pm.humanangeryourself.api.GameService;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.InvalidActionException;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

/**
 * View model between the UI and the {@link GameService}.
 * <p>
 * It sends the actions chosen in the UI to the service and exposes the latest
 * {@link GameSnapshot} as a JavaFX property the UI can listen to. It only knows the
 * {@link GameService} interface and never accesses domain objects.
 * </p>
 */
public class GameViewModel implements GameListener {
    /** Service that receives the actions and provides the game state. */
    private final GameService service;

    // latest snapshot of the game state, never null
    private final ObjectProperty<GameSnapshot> snapshot = new SimpleObjectProperty<>();

    // message of the last rejected action, empty if the last action was accepted
    private final StringProperty errorMessage = new SimpleStringProperty("");

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
        this.snapshot.set(Objects.requireNonNull(snapshot, "snapshot"));
    }

    /**
     * Sends an action to the service. If the service rejects it, the reason is exposed
     * through {@link #errorMessageProperty()}.
     *
     * @param action action selected by the player, should be one of the available actions of the snapshot
     */
    public void submit(Action action) {
        errorMessage.set("");
        try {
            service.submit(Objects.requireNonNull(action, "action"));
        } catch (InvalidActionException exception) {
            errorMessage.set(exception.getMessage());
        }
    }

    /**
     * Returns the latest game state as a read-only property.
     *
     * @return property holding the current snapshot
     */
    public ReadOnlyObjectProperty<GameSnapshot> snapshotProperty() {
        return snapshot;
    }

    /**
     * Returns the message of the last rejected action as a read-only property.
     *
     * @return property holding the error message, empty if the last action was accepted
     */
    public ReadOnlyStringProperty errorMessageProperty() {
        return errorMessage;
    }
}
