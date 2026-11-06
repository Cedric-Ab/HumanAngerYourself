package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyProperty;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Economy {

    public Economy() {
    }

    public boolean canBuy(Player player, Tile tile, Board board, PropertySize size) {
        return false;
    }

    public void buyProperty() { }

    public void buyCard() { }

    public void collectRent() { }

    public boolean isOverDebtLimit(Player player) {
        return false;
    }

    public void bankrupt(Board board, Player player) { }

    // TODO: think about the returntype
    public List<CardType> getBuyableCards(@NotNull Player player) {
        // TODO: medium priority
        return List.of();
    }

    public List<BuyProperty> getBuyableProperties(@NotNull Player player) {
        // TODO: low priority
        return List.of();
    }
}
