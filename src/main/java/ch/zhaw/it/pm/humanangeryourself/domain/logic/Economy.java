package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyProperty;
import ch.zhaw.it.pm.humanangeryourself.api.action.InvalidActionException;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.CardOffer;
import ch.zhaw.it.pm.humanangeryourself.config.GameConfig;
import ch.zhaw.it.pm.humanangeryourself.config.PropertyPrice;
import ch.zhaw.it.pm.humanangeryourself.domain.card.Card;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Property;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.Optional;

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
     * Executes the purchase of a property. Assumes the player can do that (performs no checks).
     *
     * @param player the player who wants to buy
     * @param tile   the tile the property is on
     * @param size   the size of the property to buy
     */
    public void buyProperty(Player player, Tile tile, PropertySize size) {
        player.changeBalanceBy(-config.propertyPrices().get(size).price());
        tile.setProperty(new Property(player, size));
    }

    /**
     * Executes the purchase of a property. Not implemented yet.
     */
    public void buyProperty() { }

    /**
     * Pays for a card bought from the NPC on the given tile.
     * <p>
     * Only the money is changed here.
     * </p>
     *
     * @param player   the player who buys the card
     * @param npcTile  the tile the selling NPC stands on
     * @param cardType the type of card to buy
     */
    public void buyCard(Player player, Tile npcTile, CardType cardType) {
        CardOffer offer = findBuyableOffer(player, npcTile, cardType)
                .orElseThrow(() -> new InvalidActionException(player, new BuyCard(npcTile.getId(), cardType),
                        player.getName() + " cannot buy " + cardType + " from the NPC on tile " + npcTile.getId()));
        player.changeBalanceBy(-offer.price());
    }

    /**
     * Collects rent from a player who landed on a property owned by another player.
     * <p>
     * If there is no property on the tile, this function does nothing and returns without throwing.
     * </p>
     *
     * @param debtor the player who moved their piece
     * @param tile   the tile the player landed on
     */
    public void collectRent(Player debtor, Tile tile) {
        if (tile.getProperty().isEmpty()) {
            return;
        }

        Property property = tile.getProperty().get();
        long rent = property.getRent(config);
        Player owner = property.getOwner();

        if (owner == debtor) {
            return;
        }

        debtor.changeBalanceBy(-rent);
        owner.changeBalanceBy(rent);
    }

    /**
     * Returns whether the player's debt exceeds the allowed limit.
     *
     * @param player the player to check
     * @return {@code true} if the player is over the debt limit and has to go bankrupt
     */
    public boolean isOverDebtLimit(Player player) {
        return player.getMoney() < -config.debtLimit();
    }

    /**
     * Declares the given player bankrupt and updates the board accordingly.
     * <p>
     * This includes:
     * <ul>
     *     <li>sending all the player's pieces home</li>
     *     <li>resetting the players balance to the starting balance</li>
     *     <li>removing all the player's properties from the board</li>
     * </ul>
     * </p>
     *
     * @param board  the board the player is playing on
     * @param player the player who goes bankrupt
     */
    public void bankrupt(Board board, Player player) {
        player.getPieces().forEach(Piece::sendHome);

        player.resetBalanceToZero();
        player.changeBalanceBy(config.startMoney());

        removePlayersProperties(player, board);
    }

    /**
     * Removes all properties owned by the given player from the board.
     *
     * @param player the player whose properties should be removed
     * @param board  the board the player is playing on
     */
    private void removePlayersProperties(Player player, Board board) {
        board.getTiles().stream()
                .filter(tile -> tile.getProperty().isPresent())
                .filter(tile -> tile.getProperty().get().getOwner().equals(player))
                .forEach(Tile::removeProperty);
    }


    /**
     * Returns all cards the given player can buy from NPCs right now.
     * <p>
     * A card can be bought if an NPC that sells it stands on the same tile as one of the player's pieces,
     * the player is not in debt and the player can pay the price.
     * Whether the player has already bought a card in this turn is not checked here.
     * </p>
     *
     * @param player the player who wants to buy cards, must not be {@code null}
     * @return the possible {@link BuyCard} actions, empty if none are available
     */
    public List<BuyCard> getBuyableCards(Player player) {
        return player.getPieces().stream()
                .map(Piece::getCurrentTile)
                .distinct()
                .flatMap(tile -> tile.getNpc().stream()
                        .flatMap(npc -> npc.getOffers().stream())
                        .filter(offer -> canBuyCard(player, offer))
                        .map(offer -> new BuyCard(tile.getId(), offer.cardType())))
                .toList();
    }

    /**
     * Returns all property purchases the given player can currently make.
     *
     * @param player the player who wants to buy properties
     * @param board  the board
     * @return the possible {@link BuyProperty} actions, empty if none are available
     */
    public List<BuyProperty> getBuyableProperties(Player player, Board board) {
        long budget = player.getMoney();
        Map<PropertySize, PropertyPrice> propertyPrices = config.propertyPrices();

        if (budget < propertyPrices.get(PropertySize.SMALL).price()) {
            // the player can afford nothing
            return List.of();
        }

        // tiles where the player can buy properties
        // (cannot be a Stream, because it is consumed multiple times when creating the Cartesian product below)
        List<Tile> availableTiles = findPlayersPositions(player)
                .filter(tile -> isTileBuyable(tile, board))
                .toList();

        // property sizes the player can afford
        Stream<PropertySize> availableSizes = propertyPrices.entrySet().stream()
                .filter(entry -> entry.getValue().price() <= budget)
                .map(Map.Entry::getKey);

        // find the Cartesian product of available tiles and available sizes, then map to actions
        return availableSizes.flatMap(entry -> availableTiles.stream()
                        .map(tile -> new BuyProperty(tile.getId(), entry)))
                .toList();
    }


    /**
     * Finds the offer for the given card at the NPC on the given tile if the player may buy it.
     *
     * @param player   the player who wants to buy the card
     * @param npcTile  the tile the NPC stands on
     * @param cardType the type of card to buy
     * @return the offer, or an empty Optional if the player may not buy the card there
     */
    private Optional<CardOffer> findBuyableOffer(Player player, Tile npcTile, CardType cardType) {
        if (player.getPieces(npcTile).isEmpty()) {
            return Optional.empty();
        }
        return npcTile.getNpc()
                .flatMap(npc -> npc.findOffer(cardType))
                .filter(offer -> canBuyCard(player, offer));
    }

    /**
     * Returns whether the player's money allows buying the offered card.
     * Players in debt cannot buy anything and a purchase must not lead into debt.
     *
     * @param player the player who wants to buy
     * @param offer  the offered card with its price
     * @return {@code true} if the player can pay for the card
     */
    private boolean canBuyCard(Player player, CardOffer offer) {
        return !player.isInDebt() && player.getMoney() >= offer.price();
    }


    /**
     * Checks whether a property could be placed on a given tile right now.
     *
     * @param tile  the tile to check for availability
     * @param board the board
     * @return true if the tile is buyable, false otherwise
     */
    private boolean isTileBuyable(Tile tile, Board board) {
        return !tile.isSafe() && tile.getProperty().isEmpty() && !isQuarterFull(tile.getQuarter(), board);
    }

    /**
     * Checks whether a given quarter already has the maximum number of properties.
     *
     * @param quarter the quarter to check
     * @param board   the board
     * @return true if the quarter can have no more properties, false otherwise
     */
    private boolean isQuarterFull(int quarter, Board board) {
        if (quarter < 0 || quarter >= 4) {
            throw new IllegalArgumentException("Invalid quarter: " + quarter);
        }
        Objects.requireNonNull(board);

        return board.getTilesOfQuarter(quarter).stream()
                .filter(t -> t.getProperty().isPresent())
                .count() >= config.maxPropertiesPerQuarter();
    }

    /**
     * Finds all tiles a given player's pieces are currently on.
     *
     * @param player the player
     * @return a stream of tiles
     */
    private Stream<Tile> findPlayersPositions(Player player) {
        return player.getPieces()
                .stream()
                .map(Piece::getCurrentTile)
                .distinct();
    }
}
