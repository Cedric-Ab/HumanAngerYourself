package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyProperty;
import ch.zhaw.it.pm.humanangeryourself.api.action.InvalidActionException;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.CardOffer;
import ch.zhaw.it.pm.humanangeryourself.config.GameConfig;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;

import org.jetbrains.annotations.NotNull;

import java.util.List;
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
    public List<BuyCard> getBuyableCards(@NotNull Player player) {
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
     * @param player the player who wants to buy properties, must not be {@code null}
     * @return the possible {@link BuyProperty} actions, empty if none are available
     */
    public List<BuyProperty> getBuyableProperties(@NotNull Player player) {
        // TODO: low priority
        return List.of();
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

}
