package ch.zhaw.it.pm.humanangeryourself.domain.game;

import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GamblingOutcome;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Turn {

    private final @NotNull Player player;
    private @Nullable GamblingOutcome gamblingOutcome;

    public Turn(@NotNull Player player) {
        this.player = player;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public @Nullable GamblingOutcome getGamblingOutcome() {
        return gamblingOutcome;
    }

    public void setGamblingOutcome(@NotNull GamblingOutcome gamblingOutcome) {
        this.gamblingOutcome = gamblingOutcome;
    }

    // TODO: add effect stuff
}
