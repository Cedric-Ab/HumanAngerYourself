package ch.zhaw.it.pm.humanangeryourself.api.snapshot;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;

import java.util.List;

/**
 * Contains all relevant game information for the UI.
 *
 * @param viewer              player who should see the screen now and take an action
 * @param currentPlayer       player whose turn it currently is
 * @param players             general information about all players
 * @param pieces              general information about all game pieces on the board
 * @param properties          general information about all properties on the board
 * @param npcs                general information about all NPCs on the board
 * @param viewerCards         cards the viewer is currently holding
 * @param primaryActionResult result of the player's primary action, is null iff the player has not yet rolled or gambled
 * @param decision            actions the player may take
 * @param winner              player who won the game, is null iff the game is not over
 */
public record GameSnapshot(
        PlayerColor viewer,
        PlayerColor currentPlayer,
        List<PlayerInfo> players,
        List<PieceInfo> pieces,
        List<PropertyInfo> properties,
        List<NpcInfo> npcs,
        List<CardType> viewerCards,
        PrimaryActionResult primaryActionResult,
        DecisionInfo decision,
        PlayerColor winner
) { }
