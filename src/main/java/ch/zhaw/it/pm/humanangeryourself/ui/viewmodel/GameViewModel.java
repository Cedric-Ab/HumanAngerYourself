package ch.zhaw.it.pm.humanangeryourself.ui.viewmodel;

import ch.zhaw.it.pm.humanangeryourself.api.GameListener;
import ch.zhaw.it.pm.humanangeryourself.api.GameService;
import ch.zhaw.it.pm.humanangeryourself.api.action.Roll;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Objects;

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

    public void roll() { service.submit(new Roll()); }
}
