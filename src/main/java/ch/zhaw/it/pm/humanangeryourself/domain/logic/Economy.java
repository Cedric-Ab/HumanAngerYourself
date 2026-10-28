package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;
import ch.zhaw.it.pm.humanangeryourself.config.GameConfig;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;

public class Economy {

    public Economy(GameConfig config) {
    }

    public boolean canBuy(Player player, Tile tile, Board board, PropertySize size) {
        return false;
    }

    public void buyProperty() {
    }

    public void buyCard() {
    }

    public void collectRent() {
    }

    public boolean isOverDebtLimit(Player player) {
        return false;
    }

    public void bankrupt(Board board, Player player) {
    }
}
