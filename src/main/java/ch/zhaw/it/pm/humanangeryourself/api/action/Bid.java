package ch.zhaw.it.pm.humanangeryourself.api.action;

/**
 * Represent the action where a player bids in an auction.
 *
 * @param amount how much money the player bids
 */
public record Bid(
        long amount
) implements Action { }
