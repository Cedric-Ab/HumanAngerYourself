package ch.zhaw.it.pm.humanangeryourself.domain.model;

import java.util.List;

/**
 * Gives access to the parts of the game model, namely the board and the players.
 */
public interface ModelOwner {

    /**
     * Returns the board of the game.
     *
     * @return the board
     */
    Board getBoard();

    /**
     * Returns the players of the game.
     *
     * @return the list of players
     */
    List<Player> getPlayers();
}
