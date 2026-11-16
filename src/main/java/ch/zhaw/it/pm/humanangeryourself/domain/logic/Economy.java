package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyProperty;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Game logic for everything related to money: buying properties and cards,
 * collecting rent, checking debt limits and handling bankruptcy.
 */
public class Economy {

    /**
     * Creates a new economy logic instance.
     */
    public Economy() {
    }

    /**
     * Checks whether the player is allowed to buy a property of the given size on the given tile.
     *
     * @param player the player who wants to buy
     * @param tile   the tile the property is on
     * @param board  the board the tile belongs to
     * @param size   the size of the property to buy
     * @return {@code true} if the purchase is possible
     */
    public boolean canBuy(Player player, Tile tile, Board board, PropertySize size) {
        return false;
    }

    /**
     * Executes the purchase of a property. Not implemented yet.
     */
    public void buyProperty() { }

    /**
     * Executes the purchase of an action card. Not implemented yet.
     */
    public void buyCard() { }

    /**
     * Collects rent from a player who landed on a property owned by another player.
     * Not implemented yet.
     */
    public void collectRent() { }

    /**
     * Returns whether the player's debt exceeds the allowed limit.
     *
     * @param player the player to check
     * @return {@code true} if the player is over the debt limit and has to go bankrupt
     */
    public boolean isOverDebtLimit(Player player) {
        return false;
    }

    /**
     * Declares the given player bankrupt and updates the board accordingly.
     *
     * @param board  the board the player is playing on
     * @param player the player who goes bankrupt
     */
    public void bankrupt(Board board, Player player) { }

    /**
     * Returns all card purchases the given player can currently make.
     *
     * @param player the player who wants to buy cards, must not be {@code null}
     * @return the possible {@link BuyCard} actions, empty if none are available
     */
    public List<BuyCard> getBuyableCards(@NotNull Player player) {
        // TODO: medium priority
        return List.of();
    }

    /**
     * Returns all property purchases the given player can currently make.
     *
     * @param player the player who wants to buy properties, must not be {@code null}
     * @return the possible {@link BuyProperty} actions, empty if none are available
     */
    public List<BuyProperty> getBuyableProperties(@NotNull Player player) {
        // TODO: low priority
        return List.of();
    }
}
