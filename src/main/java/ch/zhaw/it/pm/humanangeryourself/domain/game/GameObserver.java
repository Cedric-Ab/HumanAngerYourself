package ch.zhaw.it.pm.humanangeryourself.domain.game;

/**
 * Notified by the {@link GameController} whenever the game state has changed.
 */
@FunctionalInterface
public interface GameObserver {

    /**
     * Called once after every submitted action, when all phase transitions are done.
     */
    void onGameChanged();
}