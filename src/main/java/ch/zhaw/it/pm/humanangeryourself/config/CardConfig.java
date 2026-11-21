package ch.zhaw.it.pm.humanangeryourself.config;

public record CardConfig(
        int jackpot,
        int charity,
        int maxHandSize
) {
    public static CardConfig defaults() {
        return new CardConfig(250000, -150000, 10);
    }
}

