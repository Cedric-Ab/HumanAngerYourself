package ch.zhaw.it.pm.humanangeryourself.domain.model;

import java.util.List;

public interface ModelOwner {

    Board getBoard();

    List<Player> getPlayers();
}
