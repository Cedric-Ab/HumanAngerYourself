package ch.zhaw.it.pm.humanangeryourself.domain.model;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final PlayerColor color;
    private final List<CardType> cards = new ArrayList<>();
    private final List<Piece> pieces;
    private final long money;

    public Player(String name, PlayerColor color, long money, List<Piece> pieces) {
        this.name = name;
        this.color = color;
        this.money = money;
        this.pieces = new ArrayList<>(pieces);
    }

    public boolean isInDebt() {
        // TODO
        return false;
    }

    public void addMoney(long amount) {
        // TODO
    }

    public void addCard(CardType card) {
        // TODO
    }

    public void removeCard(CardType card) {
        // TODO
    }

    public boolean hasCard(CardType card) {
        // TODO
        return false;
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
