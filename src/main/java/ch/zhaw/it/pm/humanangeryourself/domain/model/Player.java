package ch.zhaw.it.pm.humanangeryourself.domain.model;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.domain.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A player with money, action cards and pieces.
 */
public class Player {
    private final String name;
    private final PlayerColor color;
    private final List<Card> cards = new ArrayList<>();
    private final List<Piece> pieces;
    private long money;

    /**
     * Creates a new player without any cards.
     *
     * @param name   name of the player
     * @param color  color of the player
     * @param money  starting money
     * @param pieces pieces of the player
     * @throws NullPointerException if an argument is {@code null}
     */
    public Player(String name, PlayerColor color, long money, List<Piece> pieces) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.color = Objects.requireNonNull(color, "color must not be null");
        this.money = money;
        this.pieces = new ArrayList<>(Objects.requireNonNull(pieces, "pieces must not be null"));
    }

    /**
     * Returns whether the player has a negative balance.
     *
     * @return {@code true} if the player's money is below zero
     */
    public boolean isInDebt() {
        return money < 0;
    }

    /**
     * Changes the player's money by the given amount. A negative amount removes money,
     * the balance is allowed to become negative (debt).
     *
     * @param amount amount to add, may be negative
     */
    public void changeBalanceBy(long amount) {
        money = Math.addExact(money, amount);
    }

    public void resetBalanceToZero() {
        money = 0;
    }

    /**
     * Returns the card of the given type that the player holds.
     * Throws an exception if the player does not hold such a card.
     *
     * @param cardType the type of card to look for
     * @return the card of the given type
     */
    public Card getCard(CardType cardType) {
        return cards.stream()
                .filter(card -> card.type() == cardType)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(name + " does not have a " + cardType + " card"));
    }

    /**
     * Gives the player a card. The hand limit is not checked here, it is handled in the discard phase.
     *
     * @param card card to add
     * @throws NullPointerException if {@code card} is {@code null}
     */
    public void addCard(Card card) {
        cards.add(Objects.requireNonNull(card, "card must not be null"));
    }

    /**
     * Removes a card from the player's hand. Throws an exception if the player does not hold such a card.
     *
     * @param card card to remove
     * @throws IllegalStateException if the player does not hold such a card
     */
    public void removeCard(Card card) {
        if (!cards.remove(card)) {
            throw new IllegalStateException(name + " does not have card " + card);
        }
    }

    /**
     * Returns whether the player has the given card in their hand.
     *
     * @param card card to look for
     * @return {@code true} if the player has the card
     */
    public boolean hasCard(Card card) {
        return cards.contains(card);
    }

    /**
     * Returns the player's piece that stands on the given tile.
     *
     * @param tile tile to look at
     * @return the piece on the tile, or an empty Optional if none of the player's pieces is there
     */
    public Optional<Piece> getPiece(Tile tile) {
        return pieces.stream()
                .filter(piece -> piece.getCurrentTile().equals(tile))
                .findFirst();
    }

    /**
     * Returns the player's piece that stands on the tile with the given id.
     *
     * @param tileId id of the tile to look at
     * @return the piece on the tile, or an empty Optional if none of the player's pieces is there
     */
    public Optional<Piece> getPiece(int tileId) {
        return pieces.stream()
                .filter(piece -> (piece.getCurrentTile().getId() == tileId))
                .findFirst();
    }

    /**
     * Returns all the player's pieces that stand on the given tile.
     *
     * @param tile tile to look at
     * @return an unmodifiable list of the player's pieces on the tile, empty if none of them is there
     */
    public List<Piece> getPieces(Tile tile) {
        return pieces.stream()
                .filter(piece -> piece.getCurrentTile().equals(tile))
                .toList();
    }

    /**
     * Returns the name of the player.
     *
     * @return the player's name, never {@code null}
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the color of the player.
     *
     * @return the player's color, never {@code null}
     */
    public PlayerColor getColor() {
        return color;
    }

    /**
     * Returns the player's current money. The value is negative if the player is in debt.
     *
     * @return the current balance
     */
    public long getMoney() {
        return money;
    }

    /**
     * Returns the action cards the player currently holds. A card type appears once per held card.
     *
     * @return an unmodifiable copy of the player's cards
     */
    public List<Card> getCards() {
        return List.copyOf(cards);
    }

    /**
     * Returns all pieces of the player.
     *
     * @return an unmodifiable copy of the player's pieces
     */
    public List<Piece> getPieces() {
        return List.copyOf(pieces);
    }
}
