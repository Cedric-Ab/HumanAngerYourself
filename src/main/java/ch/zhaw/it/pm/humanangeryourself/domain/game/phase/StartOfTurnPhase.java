package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.Gamble;
import ch.zhaw.it.pm.humanangeryourself.api.action.PlayCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.Roll;
import ch.zhaw.it.pm.humanangeryourself.domain.card.Card;
import ch.zhaw.it.pm.humanangeryourself.domain.card.HasPreconditions;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Game phase at the very start of a turn. The current player can roll the die, gamble or play a card.
 * Rolling continues with the {@link ChooseMovePhase}, gambling with the {@link GamblingPhase}.
 * Playing a card is not implemented yet and leads to the {@link PrimaryActionChoicePhase}.
 */
public class StartOfTurnPhase extends GamePhaseBase {
    /**
     * Creates a new instance of a game phase associated with the specified game.
     *
     * @param game the game instance this phase belongs to
     */
    public StartOfTurnPhase(@NotNull Game game) {
        super(game);
    }

    /**
     * Offers the current player the choice between {@link Roll} and {@link Gamble}.
     * Playing cards is not offered yet.
     *
     * @return this phase, as it waits for the player's decision
     */
    @Override
    public @NotNull GamePhase process() {
        List<Action> availableActions = new ArrayList<>();
        availableActions.add(new Roll());
        availableActions.add(new Gamble());
        availableActions.addAll(
                getCurrentPlayer().getCards().stream()
                        .filter(this::isCardPlayable)
                        .map(Card::type)
                        .map(PlayCard::new)
                        .toList()
        );
        setDecisionInfo(getCurrentPlayer(), availableActions);
        return this;
    }

    private boolean isCardPlayable(Card card) {
        if (card instanceof HasPreconditions withPreconditions) {
            return withPreconditions.isApplyable(getGame(), getCurrentPlayer());
        } else {
            return true;
        }
    }

    /**
     * Handles the player's choice and moves on to the matching phase.
     *
     * @param player the player who made the decision
     * @param action the chosen action: {@link Roll}, {@link Gamble} or {@link PlayCard}
     * @return a {@link ChooseMovePhase} after a roll, a {@link GamblingPhase} after a gamble,
     *         or a {@link PrimaryActionChoicePhase} after playing a card
     * @throws RuntimeException if the action is of an unexpected type
     */
    @Override
    public @NotNull GamePhase handle(Player player, Action action) {
        validatePlayer(player);
        validateAction(action);

        return switch (action) {
            case Roll ignored -> new ChooseMovePhase(getGame());
            case Gamble ignored -> new GamblingPhase(getGame());
            case PlayCard(CardType card) -> playCard(player, player.getCard(card));
            default -> throw new RuntimeException("Unexpected action type: " + action.getClass().getName());
        };
    }

    private GamePhase playCard(Player player, Card card) {
        player.removeCard(card);
        return new ActionCardPhase(getGame(), new PrimaryActionChoicePhase(getGame()), player, card);
    }
}
