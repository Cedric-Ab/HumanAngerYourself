package ch.zhaw.it.pm.humanangeryourself.api.snapshot;

/**
 * Represents the outcome of using the gambling mechanic for the UI to display.
 *
 * @param isCard    whether the result is an action card (as opposed to money)
 * @param message   a simple message describing the outcome
 */
public record GamblingOutcome(boolean isCard, String message) { }
