package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.domain.card.ActionModifier;
import ch.zhaw.it.pm.humanangeryourself.domain.card.ApplicableCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.Counter;
import ch.zhaw.it.pm.humanangeryourself.domain.card.CounterChain;
import ch.zhaw.it.pm.humanangeryourself.domain.card.Targeted;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ActionCardPhase extends NonFlowGamePhase {
    private final Player player;
    private final ApplicableCard actionCard;
    private final CounterChain counterChain;
    private boolean countersResolved = false;

    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game       the game instance this phase belongs to
     * @param nextPhase  the next game phase to transition to after this phase is completed
     * @param player     the player who is playing the action card in this phase
     * @param actionCard the action card that is being played in this phase
     */
    public ActionCardPhase(@NotNull Game game, @NotNull GamePhase nextPhase, Player player, ApplicableCard actionCard) {
        super(game, nextPhase);
        this.player = player;
        this.actionCard = actionCard;
        this.counterChain = new CounterChain(actionCard);
    }

    @Override
    public @NotNull GamePhase process() {
        if (actionCard instanceof Targeted<?> targeted && targeted.requiresFurtherTarget(getGame(), player)) {
            setDecisionInfo(player, targeted.getPossibleTargets(getGame(), player));
            return this;
        } else {
            resolveCounters();
            return applyCard();
        }
    }

    private GamePhase applyCard() {
        Optional<ApplicableCard> effectiveCard = counterChain.resolve();
        if (effectiveCard.isEmpty()) {
            return getNextPhase();
        }

        ApplicableCard card = effectiveCard.get();
        card.apply(getGame(), player);


        if (card instanceof ActionModifier modifier) {
            GamePhase gamePhase = getNextPhase().process();
            if (gamePhase != getNextPhase()) {
                throw new IllegalStateException("Next phase should not be skipped before forced action is applied.");
            }
            return getNextPhase().handle(player, modifier.forcedAction());
        } else {
            return getNextPhase();
        }
    }

    @Override
    public @NotNull GamePhase handle(Player player, Action action) {
        validatePlayer(player);
        validateAction(action);

        if (!(actionCard instanceof Targeted<?> targeted)) {
            throw new IllegalStateException("Action card does not require a target.");
        }

        targeted.setTarget(action);

        return process();
    }

    private void resolveCounters() {
        if (countersResolved) {
            return;
        }
        countersResolved = true;

        if (!actionCard.type().isCounterable()) {
            return;
        }
        Optional<Player> defender = actionCard.getDefender(getGame(), player);
        if (defender.isEmpty()) {
            return;
        }

        Player reacting = defender.get();
        Player other = player;
        Optional<Counter> counter = takeCounter(reacting);
        while (counter.isPresent()) {
            counterChain.add(counter.get());
            Player previouslyReacting = reacting;
            reacting = other;
            other = previouslyReacting;
            counter = takeCounter(reacting);
        }
    }

    private static Optional<Counter> takeCounter(Player holder) {
        Optional<Counter> counter = holder.getCards().stream()
                .filter(Counter.class::isInstance)
                .map(Counter.class::cast)
                .findFirst();
        counter.ifPresent(holder::removeCard);
        return counter;
    }
}
