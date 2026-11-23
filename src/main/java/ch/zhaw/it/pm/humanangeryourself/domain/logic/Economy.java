package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyProperty;
import ch.zhaw.it.pm.humanangeryourself.config.GameConfig;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Game logic for everything related to money: buying properties and cards,
 * collecting rent, checking debt limits and handling bankruptcy.
 */
public class Economy {

    private final GameConfig config;

    /**
     * Creates a new economy logic instance with the default configuration.
     */
    public Economy() {
        this(GameConfig.defaults());
    }

    /**
     * Creates a new economy logic instance with a custom configuration.
     *
     * @param config the custom configuration to use
     */
    public Economy(GameConfig config) {
        this.config = config;
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
     * @param board  the board, must not be {@code null}
     * @return the possible {@link BuyCard} actions, empty if none are available
     */
    public List<BuyCard> getBuyableCards(Player player, Board board) {
        // TODO: needs to be tested
        long budget = player.getMoney();

        if (budget <= 0) {
            return List.of();
        }

        return findPlayersPositions(player)
                .map(Tile::getNpc)
                .flatMap(Optional::stream)
                .flatMap(npc -> npc.getOffers().stream())
                .filter(offer -> offer.price() <= budget)
                .map(offer -> new BuyCard(offer.cardType()))
                .toList();
    }

    /**
     * Returns all property purchases the given player can currently make.
     *
     * @param player the player who wants to buy properties, must not be {@code null}
     * @return the possible {@link BuyProperty} actions, empty if none are available
     */
    public List<BuyProperty> getBuyableProperties(Player player, Board board) {
        // TODO: needs to be tested
        long budget = player.getMoney();
        Map<PropertySize, PropertyPrice> propertyPrices = config.propertyPrices();

        if (budget < propertyPrices.get(PropertySize.SMALL).price()) {
            return List.of();
        }

        List<Tile> availableTiles = findPlayersPositions(player)
                .filter(tile -> isBuyable(tile, board))
                .toList();

        return propertyPrices.entrySet().stream()
                .filter(entry -> entry.getValue().price() <= budget)
                .flatMap(entry -> availableTiles.stream()
                        .map(tile -> new BuyProperty(tile.getId(), entry.getKey())))
                .toList();
    }

    // TODO: Tile::isBuyable currently makes no sense. This function needs to be adapted after Tile::isBuyable is corrected
    public boolean isBuyable(Tile tile, Board board) {
        return tile.isBuyable() && board.getTilesByQuarter(tile.getQuarter());
    }

    private Stream<Tile> findPlayersPositions(Player player) {
        return player.getPieces()
                .stream()
                .map(Piece::getCurrentTile);
    }
}
