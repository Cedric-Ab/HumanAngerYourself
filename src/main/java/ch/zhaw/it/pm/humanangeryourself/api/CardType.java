package ch.zhaw.it.pm.humanangeryourself.api;

/**
 * Types of action cards.
 * <p>
 * Each type has three properties: whether it is played instantly when drawn, whether it needs a
 * target player, and whether other players can counter it.
 */
public enum CardType {
    /**
     * Doubles the effect of the player's next move or roll. Can be countered.
     */
    DOUBLE_IT(false, false, true),
    /**
     * Lets the player roll again.
     */
    TRY_AGAIN(false, false, false),
    /**
     * Gives the player extra money.
     */
    MONEY_TIGER(false, false, false),
    /**
     * Reverses the direction of an attack. Can be countered.
     */
    ANGER_REVERSE(false, false, true),
    /**
     * Gives the player a movement bonus.
     */
    SPEED_BOOST(false, false, false),
    /**
     * Swaps positions with a piece of another player, who has to be chosen as target. Can be countered.
     */
    SWITCH(false, true, true),
    /**
     * Pays out a jackpot to the player. Takes effect instantly when drawn.
     */
    JACKPOT(true, false, false),
    /**
     * Makes the player lose money.
     */
    CHARITY(false, false, false);

    private final boolean instant;
    private final boolean targeting;
    private final boolean counterable;

    /**
     * Creates a card type with the given properties.
     *
     * @param isInstant     whether the card takes effect immediately when it is drawn
     * @param needsTarget   whether the player has to choose a target when playing the card
     * @param isCounterable whether other players can counter the card
     */
    CardType(boolean isInstant, boolean needsTarget, boolean isCounterable) {
        instant = isInstant;
        targeting = needsTarget;
        counterable = isCounterable;
    }

    /**
     * Returns whether the card takes effect immediately when it is drawn instead of being kept in hand.
     *
     * @return {@code true} if the card is played instantly
     */
    public boolean isInstant() {
        return instant;
    }

    /**
     * Returns whether the player has to choose a target when playing the card.
     *
     * @return {@code true} if the card needs a target
     */
    public boolean needsTarget() {
        return targeting;
    }

    /**
     * Returns whether other players can counter the card after it has been played.
     *
     * @return {@code true} if the card can be countered
     */
    public boolean isCounterable() {
        return counterable;
    }
}
