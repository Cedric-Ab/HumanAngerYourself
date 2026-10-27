package ch.zhaw.it.pm.humanangeryourself.api.snapshot;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;

import java.util.List;

/**
 * Contains all relevant game information for the UI.
 *
 * @param viewer player who should see the screen now and take an action
 * @param currentPlayer player whose turn it currently is
 * @param players general information about all players
 * @param pieces general information about all game pieces on the board
 * @param properties general information about all properties on the board
 * @param npcs general information about all NPCs on the board
 * @param viewerCards cards the viewer is currently holding
 * @param lastRoll last dice roll of the game (not necessarily the viewer's last roll)
 * @param decision actions the player may take
 * @param winner player who won the game
 */
public record GameSnapshot(
        PlayerColor viewer,
        PlayerColor currentPlayer,
        List<PlayerInfo> players,
        List<PieceInfo> pieces,
        List<PropertyInfo> properties,
        List<NpcInfo> npcs,
        List<CardType> viewerCards,
        int lastRoll,
        DecisionInfo decision,
        PlayerColor winner
) { }
