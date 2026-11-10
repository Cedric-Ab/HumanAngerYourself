package ch.zhaw.it.pm.humanangeryourself.domain.model;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;

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
    private final List<CardType> cards = new ArrayList<>();
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
    public void addMoney(long amount) {
        money = Math.addExact(money, amount);
    }

    /**
     * Gives the player a card. The hand limit is not checked here, it is handled in the discard phase.
     *
     * @param card card to add
     * @throws NullPointerException if {@code card} is {@code null}
     */
    public void addCard(CardType card) {
        cards.add(Objects.requireNonNull(card, "card must not be null"));
    }

    /**
     * Removes one card of the given type from the player's hand.
     *
     * @param card card to remove
     * @throws IllegalStateException if the player does not hold such a card
     */
    public void removeCard(CardType card) {
        if (!cards.remove(card)) {
            throw new IllegalStateException(name + " does not have card " + card);
        }
    }

    /**
     * Returns whether the player holds at least one card of the given type.
     *
     * @param card card to look for
     * @return {@code true} if the player has the card
     */
    public boolean hasCard(CardType card) {
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

    public String getName() {
        return name;
    }

    public PlayerColor getColor() {
        return color;
    }

    public long getMoney() {
        return money;
    }

    public List<CardType> getCards() {
        return List.copyOf(cards);
    }

    public List<Piece> getPieces() {
        return List.copyOf(pieces);
    }
}
