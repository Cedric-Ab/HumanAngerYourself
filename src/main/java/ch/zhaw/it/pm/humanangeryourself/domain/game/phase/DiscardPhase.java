package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.DiscardCard;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.DecisionInfo;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents a game phase where a player is required to discard a card.
 * <p>
 * This phase is responsible for handling the discarding action of the specified player.
 * The phase can optionally restrict a certain card from being discarded.
 */
public class DiscardPhase extends NonFlowGamePhase {

    private final @NotNull Player discardingPlayer;
    private final @Nullable CardType notDiscardableCard;

    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game      the game instance this phase belongs to
     * @param nextPhase the next game phase to transition to after this phase is completed
     * @param player    the  player who is currently discarding a card
     */
    public DiscardPhase(@NotNull Game game, @NotNull GamePhase nextPhase,
                        @NotNull Player player) {
        this(game, nextPhase, player, null);
    }

    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game               the game instance this phase belongs to
     * @param nextPhase          the next game phase to transition to after this phase is completed
     * @param player             the  player who is currently discarding a card
     * @param notDiscardableCard an optional card type that cannot be discarded during this phase;
     *                           can be null if all cards are discardable
     */
    public DiscardPhase(@NotNull Game game, @NotNull GamePhase nextPhase,
                        @NotNull Player player, @Nullable CardType notDiscardableCard) {
        super(game, nextPhase);
        this.discardingPlayer = player;
        this.notDiscardableCard = notDiscardableCard;
    }

    @Override
    public @NotNull GamePhase process() {
        List<CardType> cards = discardingPlayer.getCards();
        cards.remove(notDiscardableCard);

        List<DiscardCard> possibleCardDiscards = cards.stream()
                .map(DiscardCard::new)
                .toList();

        setDecisionInfo(new DecisionInfo(possibleCardDiscards));

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull GamePhase handle(Player player, Action action) {
        if (discardingPlayer.equals(player)) {
            throw new IllegalArgumentException("Only the discarding player can perform actions in this phase.");
        }
        if (!getDecision().availableActions().contains(action)) {
            throw new IllegalArgumentException("The action is not available in this phase.");
        }

        if (!(action instanceof DiscardCard(CardType card))) {
            throw new IllegalArgumentException("Invalid action for this phase.");
        }

        player.removeCard(card);

        return getNextPhase();
    }
}
