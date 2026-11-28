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

/**
 * Phase in which a player plays an action card.
 *
 * <p>The phase first collects all targets the card needs. If the card can be countered, counters
 * are played automatically: the attacked player (the defender) blocks if they hold a counter card,
 * then the original player blocks that block if they hold one, and so on. As soon as one side has
 * no counter card left, the {@link CounterChain} decides whether the card takes effect or is blocked.</p>
 */
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

    /**
     * Processes the phase up to the point where a decision of the player is required.
     *
     * <p>If the card still needs a target, the possible targets are offered to the player and this
     * phase is returned. Otherwise the counters are resolved and the card that remains in effect
     * is applied.</p>
     *
     * @return this phase if the player still has to select a target, otherwise the next phase
     */
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

    /**
     * Applies the card that remains in effect after all counters, if any.
     *
     * @return the next phase
     */
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

    /**
     * Handles the selection of a target for the played card and continues processing.
     *
     * @param player the player making the action, must be the player who has to decide
     * @param action the selected target
     * @return this phase if another target is required, otherwise the next phase
     * @throws IllegalStateException if the played card does not take any targets
     */
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

    /**
     * Plays counter cards automatically, alternating between the defender and the original player,
     * until one of them has no counter card left. Does nothing if the card cannot be countered
     * or has no defender. Runs only once per phase.
     */
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

    /**
     * Removes a counter card from the player's hand, if they hold one.
     *
     * @param holder the player who has to react
     * @return the removed counter card, or empty if the player holds none
     */
    private static Optional<Counter> takeCounter(Player holder) {
        Optional<Counter> counter = holder.getCards().stream()
                .filter(Counter.class::isInstance)
                .map(Counter.class::cast)
                .findFirst();
        counter.ifPresent(holder::removeCard);
        return counter;
    }
}
