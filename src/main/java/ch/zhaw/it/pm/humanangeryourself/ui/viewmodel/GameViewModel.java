package ch.zhaw.it.pm.humanangeryourself.ui.viewmodel;

import ch.zhaw.it.pm.humanangeryourself.api.GameListener;
import ch.zhaw.it.pm.humanangeryourself.api.GameService;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.InvalidActionException;
import ch.zhaw.it.pm.humanangeryourself.api.action.MovePiece;
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

    // tile the player selected to buy something on (property or NPC card), null if none is selected
    private final ObjectProperty<Integer> selectedTile = new SimpleObjectProperty<>();

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
     * The selected tile is cleared when the next player's turn starts.
     *
     * @param snapshot the new snapshot of the game
     */
    @Override
    public void onGameChanged(GameSnapshot snapshot) {
        Objects.requireNonNull(snapshot, "snapshot");
        if (this.snapshot.get().currentPlayer() != snapshot.currentPlayer()) {
            selectedTile.set(null);
        }
        this.snapshot.set(snapshot);
    }

    /**
     * Sends an action to the service. If the service rejects it, the reason is exposed
     * through {@link #errorMessageProperty()}. After moving a piece, its target tile is selected,
     * so the player can buy something there right away.
     *
     * @param action action selected by the player, should be one of the available actions of the snapshot
     */
    public void submit(Action action) {
        Objects.requireNonNull(action, "action");
        errorMessage.set("");
        if (action instanceof MovePiece move) {
            selectedTile.set(move.targetTile());
        }
        try {
            service.submit(action);
        } catch (InvalidActionException exception) {
            errorMessage.set(exception.getMessage());
        }
    }

    /**
     * Selects the tile the player wants to buy something on.
     *
     * @param tile id of the tile
     */
    public void selectTile(int tile) {
        selectedTile.set(tile);
    }

    /**
     * Returns the tile the player selected to buy a property or an NPC card on.
     *
     * @return property holding the selected tile id, holds {@code null} if no tile is selected
     */
    public ReadOnlyObjectProperty<Integer> selectedTileProperty() {
        return selectedTile;
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
