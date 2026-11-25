package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;

import java.util.Optional;

/**
 * Represents a counter card, a special kind of {@link Card} that can be played
 * in reaction to a {@link CardType#isCounterable()} card.
 *
 * <p>A counter card does not take effect on its own. Instead, it is applied to
 * another card. The result of this interaction is returned to the caller, which allows counter
 * cards to be chained or evaluated by the game logic.</p>
 *
 * @see Card
 */
public interface Counter extends Card {
    /**
     * Applies this counter to the given card.
     *
     * <p>The returned value represents the outcome of the interaction between
     * this counter and the given card. A counter may modify the card, replace it
     * with another card, or completely negate its effect.</p>
     *
     * @param card the card this counter is applied to; must not be {@code null}
     * @return an {@link Optional} containing the card that remains in effect
     *         after the counter has been applied, or {@link Optional#empty()}
     *         if the card was completely countered
     * @throws NullPointerException if {@code card} is {@code null}
     */
    Optional<ApplicableCard> apply(ApplicableCard card);
}
