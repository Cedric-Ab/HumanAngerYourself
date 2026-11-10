package ch.zhaw.it.pm.humanangeryourself.ui.viewmodel;

import ch.zhaw.it.pm.humanangeryourself.api.GameListener;
import ch.zhaw.it.pm.humanangeryourself.api.GameService;
import ch.zhaw.it.pm.humanangeryourself.api.action.Roll;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Objects;

/**
 * View model between the game service and the user interface.
 * It keeps the latest {@link GameSnapshot} and forwards the user's actions to the service.
 */
public class GameViewModel implements GameListener {
    /** Service that receives the actions and provides the game state. */
    private final GameService service;

    /** The latest snapshot of the game state. */
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

    /**
     * Replaces the stored snapshot with the new game state.
     *
     * @param snapshot the new snapshot of the game
     */
    @Override
    public void onGameChanged(GameSnapshot snapshot) {
        this.snapshot.set(snapshot);
    }

    /**
     * Submits a {@link Roll} action to the game service.
     */
    public void roll() { service.submit(new Roll()); }
}
