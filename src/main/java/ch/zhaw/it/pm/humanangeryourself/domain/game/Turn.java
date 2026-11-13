package ch.zhaw.it.pm.humanangeryourself.domain.game;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A single turn of the game, belonging to one player.
 */
public class Turn {

    /**
     * The player whose turn it is.
     */
    private final @NotNull Player player;
    private int dieResult;
    private @Nullable CardType obtainedCard;
    private @Nullable String resultMessage;

    /**
     * Creates a new turn for the given player.
     *
     * @param player the player whose turn it is
     */
    public Turn(@NotNull Player player) {
        this.player = player;
    }

    /**
     * Returns the player whose turn it is.
     *
     * @return the player of this turn
     */
    public @NotNull Player getPlayer() {
        return player;
    }

    public int getDieResult() {
        return dieResult;
    }

    public void setDieResult(int dieResult) {
        this.dieResult = dieResult;
    }

    public @Nullable CardType getObtainedCard() {
        return obtainedCard;
    }

    public void setObtainedCard(@Nullable CardType obtainedCard) {
        this.obtainedCard = obtainedCard;
    }

    public @Nullable String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(@Nullable String resultMessage) {
        this.resultMessage = resultMessage;
    }

    // TODO: add effect stuff
}
