package ch.zhaw.it.pm.humanangeryourself.api;

/**
 * Types of action cards.
 * <p>
 * Each type has three properties: whether it is played instantly when drawn, whether it needs a
 * target player, and whether other players can counter it.
 */
public enum CardType {
    /**
     * The player’s debt is doubled and passed to the next player in clockwise order. The player’s balance is set to zero.
     */
    DOUBLE_IT(false, false, true),
    /**
     * Gambling is guaranteed to result in an action card.
     */
    TRY_AGAIN(false, false, false),
    /**
     * Gambling is guaranteed to result in a payout. The payout from gambling is doubled.
     */
    MONEY_TIGER(false, false, false),
    /**
     * Counters Double It, Switch or another Anger Reverse. Can be countered.
     */
    ANGER_REVERSE(false, false, true),
    /**
     * Doubles the number rolled for the next movement.
     */
    SPEED_BOOST(false, false, false),
    /**
     * Swaps the position of one of the player’s pieces with a piece of another player. Can be countered.
     */
    SWITCH(false, true, true),
    /**
     * Pays out a jackpot to the player. Takes effect instantly when drawn.
     */
    JACKPOT(true, false, false),
    /**
     * The player pays money. Takes effect instantly when drawn.
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
