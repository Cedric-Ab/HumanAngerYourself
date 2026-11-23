package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.domain.card.Card;
import ch.zhaw.it.pm.humanangeryourself.domain.card.InstantCard;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

public class ActionCardPhase extends NonFlowGamePhase {
    private final Player player;
    private final Card actionCard;

    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game       the game instance this phase belongs to
     * @param nextPhase  the next game phase to transition to after this phase is completed
     * @param player     the player who is playing the action card in this phase
     * @param actionCard the action card that is being played in this phase
     */
    public ActionCardPhase(@NotNull Game game, @NotNull GamePhase nextPhase, Player player, Card actionCard) {
        super(game, nextPhase);
        this.player = player;
        this.actionCard = actionCard;
    }

    @Override
    public @NotNull GamePhase process() {
        if (actionCard instanceof InstantCard instantCard) {
            return applyInstantCard(instantCard);
        }
        // TODO: implement properly
        return getNextPhase();
    }

    private GamePhase applyInstantCard(InstantCard card) {
        card.apply(getGame(), player);
        return getNextPhase();
    }

    @Override
    public @NotNull GamePhase handle(Player player, Action action) {
        return null;
    }
}
