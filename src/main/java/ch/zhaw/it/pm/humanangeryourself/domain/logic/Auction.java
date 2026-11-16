package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

/**
 * An auction in which players bid against each other.
 */
public class Auction {

    /**
     * Returns the player who currently holds the highest bid.
     *
     * @return the current highest bidder, or {@code null} if nobody has bid yet
     */
    public Player getCurrentBidder() {
        return null;
    }

    /**
     * Places a bid for the given player.
     *
     * @param player the player who bids
     * @param amount the amount offered
     */
    public void bid(Player player, int amount) { }

    /**
     * Returns whether the auction is over and no further bids are accepted.
     *
     * @return {@code true} if the auction is finished
     */
    public boolean isFinished() {
        return false;
    }
}
