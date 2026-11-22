package integration;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.EndTurn;
import ch.zhaw.it.pm.humanangeryourself.api.action.Gamble;
import ch.zhaw.it.pm.humanangeryourself.api.action.MovePiece;
import ch.zhaw.it.pm.humanangeryourself.api.action.Roll;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.DecisionInfo;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PlayerInfo;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.logic.MovementRules;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for integration tests
 * that provides utility methods for interacting with the game controller and game state.
 */
public abstract class BaseIntegrationTest {
    private final RiggedController controller = new RiggedController();

    protected GameSnapshot getSnapshot(PlayerColor viewer) {
        return controller.getSnapshot(viewer);
    }

    protected GameSnapshot getSnapshot() {
        return controller.getSnapshot();
    }

    protected DecisionInfo getDecision() {
        return controller.getDecision();
    }

    /**
     * Sets the result of the die roll to a specific value for testing purposes.
     *
     * @param result the desired result of the die roll,
     *               should be between 1 and 6 inclusive according to {@link MovementRules#rollDie()}
     */
    protected void setRollDieResult(int result) {
        controller.setRollDieResult(result);
    }

    protected void setGambleResult(int result) {
        controller.setGambleResult(result);
    }

    protected Game getGame() {
        return controller.getGame();
    }


    protected PlayerColor decider() {
        return getDecision().decider();
    }

    protected List<? extends Action> availableActions() {
        return getDecision().availableActions();
    }

    protected PlayerInfo getPlayerInfo(PlayerColor color) {
        return getSnapshot().players().stream()
                .filter(playerInfo -> playerInfo.color() == color)
                .findFirst()
                .orElseThrow();
    }

    protected PlayerInfo getDeciderInfo() {
        return getPlayerInfo(decider());
    }

    /**
     * Submits an action to the game controller.
     *
     * @param action the action to submit
     */
    protected void submitUnchecked(Action action) {
        controller.submit(decider(), action);
    }

    /**
     * Submits an action to the game controller and checks if the action is valid.
     * <p>
     * Use when the given action is expected to be valid.
     *
     * @param action the action to submit
     * @throws IllegalStateException if the action is not valid for the current decision
     */
    protected void submitChecked(Action action) {
        if (!availableActions().contains(action)) {
            throw new IllegalStateException("Test is outdated. Current decision does not allow action: " + action);
        }

        controller.submit(decider(), action);
    }

    /**
     * Rushes through a primary action by setting the gamble result to 0 and submitting a Gamble action.
     */
    protected void rushThroughPrimaryAction() {
        setGambleResult(0);
        submitChecked(new Gamble());
    }

    /**
     * Rushes through a turn by setting the gamble result to 0 and submitting a Gamble and EndTurn action.
     * <p>
     * This method is used to quickly progress through a turn in the game for testing purposes.
     * Use this method when you want to skip the details of a turn and move on to the next player's turn.
     */
    protected void rushThroughTurn() {
        rushThroughPrimaryAction();
        submitChecked(new EndTurn());
    }

    protected void rollNumber(int roll) {
        setRollDieResult(roll);
        submitChecked(new Roll());
    }

    protected Player getCurrentPlayer() {
        return getGame().getPlayer(decider());
    }

    protected List<Piece> getPieces() {
        return new ArrayList<>(
                getCurrentPlayer()
                        .getPieces()
        );
    }

    protected Piece getFirstPiece() {
        return getPieces()
                .getFirst();
    }

    protected void setPieceOntoTile(Piece piece, int tileID) {
        Tile tile = getGame().getBoard().getTileById(tileID);
        piece.moveTo(tile);
    }

    protected List<MovePiece> getMoveActions() {
        return availableActions().stream()
                .filter(MovePiece.class::isInstance)
                .map(MovePiece.class::cast)
                .toList();
    }

    protected MovePiece getMoveAction(int startTile) {
        return getMoveActions().stream()
                .filter(move -> move.startTile() == startTile)
                .findFirst()
                .orElseThrow();
    }

    protected void rollAndMoveFromTile(int startTile, int roll) {
        setPieceOntoTile(getFirstPiece(), startTile);
        rollNumber(roll);
        submitChecked(getMoveAction(startTile));
    }
}
