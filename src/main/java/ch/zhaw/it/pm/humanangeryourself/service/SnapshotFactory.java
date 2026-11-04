package ch.zhaw.it.pm.humanangeryourself.service;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.NpcInfo;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.DecisionInfo;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PieceInfo;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PlayerInfo;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PropertyInfo;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.game.GameController;

import java.util.List;
import java.util.Objects;

/**
 * Creates {@link GameSnapshot}s from the current game state.
 * <p>
 * A snapshot is a read only copy of the game for one viewer. It shall only contain API types
 * so the UI (and later the network) never gets access to domain objects.
 * </p>
 */
public final class SnapshotFactory {

    /**
     * // util class, no instances.
     */
    private SnapshotFactory() {
        // util class, no instances
    }

    /**
     * Creates a snapshot of the game as seen by the given viewer.
     *
     * @param controller the controller holding the game and the current phase
     * @param viewer     the player the snapshot is created for, {@code null} if nobody
     *                   has to decide (e.g. game over); then no cards are shown
     * @return a new immutable snapshot
     */
    public static GameSnapshot create(GameController controller, PlayerColor viewer) {
        Objects.requireNonNull(controller, "controller");
        Game game = controller.getGame();
        Board board = game.getBoard();
        List<Player> players = game.getPlayers();

        PlayerColor currentPlayer = null; // TODO, maybe from game.getTurn().getPlayer().getColor()?
        List<PlayerInfo> playerInfos = toPlayerInfos(players);
        List<PieceInfo> pieceInfos = toPieceInfos(players);
        List<PropertyInfo> propertyInfos = toPropertyInfos(board);
        List<NpcInfo> npcInfos = toNpcInfos(board);
        List<CardType> viewerCards = getViewerCards(game, viewer);
        int lastRoll = 0; // TODO; maybe game.getTurn().getLastRoll();
        DecisionInfo decisionInfo = controller.getDecision();

        return new GameSnapshot(
                viewer, currentPlayer,
                playerInfos, pieceInfos,
                propertyInfos, npcInfos, viewerCards,
                lastRoll, decisionInfo, getWinnerColor(game));
    }

    private static List<PlayerInfo> toPlayerInfos(List<Player> players) {
        return players.stream()
                .map(player -> new PlayerInfo(
                        player.getName(),
                        player.getColor(),
                        player.getMoney(),
                        player.getCards().size()))
                .toList();
    }

    private static List<PieceInfo> toPieceInfos(List<Player> players) {
        return players.stream()
                .flatMap(player -> player.getPieces().stream()
                        .map(piece -> new PieceInfo(player.getColor(), piece.getCurrentTile().getId())))
                .toList();
    }

    private static List<PropertyInfo> toPropertyInfos(Board board) {
        return board.getTiles().stream()
                .flatMap(tile -> tile.getProperty().stream()
                        .map(property -> new PropertyInfo(
                                tile.getId(),
                                property.getOwner().map(Player::getColor).orElse(null),
                                property.getSize())))
                .toList();
    }

    private static List<NpcInfo> toNpcInfos(Board board) {
        return board.getTiles().stream()
                .flatMap(tile -> tile.getNpc().stream()
                        .map(npc -> new NpcInfo(tile.getId(), List.copyOf(npc.getOffers()))))
                .toList();
    }

    private static List<CardType> getViewerCards(Game game, PlayerColor viewer) {
        if (viewer == null) { return List.of(); }

        return List.copyOf(game.getPlayer(viewer).getCards());
    }

    private static PlayerColor getWinnerColor(Game game) {
        return null;
        //TODO: maybe return game.getWinner().map(Player::getColor).orElse(null);
    }
}
