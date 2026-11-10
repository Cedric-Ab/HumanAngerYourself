package ch.zhaw.it.pm.humanangeryourself.ui.viewmodel;

import ch.zhaw.it.pm.humanangeryourself.api.GameListener;
import ch.zhaw.it.pm.humanangeryourself.api.GameService;
import ch.zhaw.it.pm.humanangeryourself.api.action.InvalidActionException;
import ch.zhaw.it.pm.humanangeryourself.api.action.Roll;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Objects;

/**
 * View model between the UI and the {@link GameService}.
 * <p>
 * It turns user input into actions that are sent to the service and exposes the latest
 * {@link GameSnapshot} as a JavaFX property the UI can bind to. It only knows the
 * {@link GameService} interface and never accesses domain objects.
 * </p>
 */
public class GameViewModel implements GameListener {

    private final GameService service;

    private final ObjectProperty<GameSnapshot> snapshot = new SimpleObjectProperty<>();

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

    @Override
    public void onGameChanged(GameSnapshot snapshot) {
        this.snapshot.set(snapshot);
    }

    /**
     * Rolls the dice for the player who currently has to decide.
     *
     * @throws InvalidActionException if rolling is not allowed in the current phase of the game
     */
    public void roll() { service.submit(new Roll()); }

    /**
     * Returns the latest game state as read only property.
     * <p>
     * The UI can bind to this property but cannot change it.
     * </p>
     *
     * @return the property holding the current snapshot
     */
    public ReadOnlyObjectProperty<GameSnapshot> snapshotProperty() {
        return snapshot;
    }
}
