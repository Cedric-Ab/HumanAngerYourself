package ch.zhaw.it.pm.humanangeryourself.api;

public enum CardType {
    DOUBLE_IT(false, false, true),
    TRY_AGAIN(false, false, false),
    MONEY_TIGER(false, false, false),
    ANGER_REVERSE(false, false, true),
    SPEED_BOOST(false, false, false),
    SWITCH(false, true, true),
    JACKPOT(true, false, false),
    CHARITY(false, false, false);

    private final boolean instant;
    private final boolean targeting;
    private final boolean counterable;

    CardType(boolean isInstant, boolean needsTarget, boolean isCounterable) {
        instant = isInstant;
        targeting = needsTarget;
        counterable = isCounterable;
    }

    public boolean isInstant() {
        return instant;
    }

    public boolean needsTarget() {
        return targeting;
    }

    public boolean isCounterable() {
        return counterable;
    }
}
