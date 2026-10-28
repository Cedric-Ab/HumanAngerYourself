package ch.zhaw.it.pm.humanangeryourself.api;

public interface GameListener {
    /**
     * Called by the domain logic to inform the UI about
     * changes of the game state.
     */
    void onGameChanged();
}
