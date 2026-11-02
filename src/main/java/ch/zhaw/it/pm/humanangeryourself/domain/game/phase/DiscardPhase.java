package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.DiscardCard;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.DecisionInfo;
import ch.zhaw.it.pm.humanangeryourself.config.GameConfig;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents a game phase where a player may be required to discard a card.
 * <p>
 * This phase is responsible for handling the discarding action of the specified player.
 * The phase can optionally restrict a certain card from being discarded.
 */
public class DiscardPhase extends NonFlowGamePhase {

    private final @NotNull Player discardingPlayer;
    private final @Nullable CardType notDiscardableCard;

    /**
     * Creates a new instance of the discard phase associated with the specified game.
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
     * Creates a new instance of the discard phase associated with the specified game.
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
        GameConfig gameConfig = GameConfig.getInstance();

        List<CardType> cards = discardingPlayer.getCards();

        if (cards.size() <= gameConfig.maxHandSize()) {
            return getNextPhase();
        } else {
            createDecisionInfo(discardingPlayer, cards);
            return this;
        }
    }

    private void createDecisionInfo(Player player, List<CardType> cards) {
        if (notDiscardableCard != null) {
            cards.remove(notDiscardableCard);
        }

        List<DiscardCard> possibleCardDiscards = cards.stream()
                .map(DiscardCard::new)
                .toList();

        setDecisionInfo(new DecisionInfo(player, possibleCardDiscards));
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the action is not available in this phase
     * @throws IllegalArgumentException if the player attempting the action is not the discarding player
     */
    @Override
    public @NotNull GamePhase handle(Player player, Action action) {
        if (discardingPlayer.equals(player)) {
            throw new IllegalArgumentException("Only the discarding player can perform actions in this phase.");
        }
        if (isIllegalAction(action) || !(action instanceof DiscardCard(CardType card))) {
            throw new IllegalArgumentException("Action is not available in this phase.");
        }

        player.removeCard(card);

        return process();
    }
}
