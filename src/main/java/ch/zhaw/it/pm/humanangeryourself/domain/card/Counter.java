package ch.zhaw.it.pm.humanangeryourself.domain.card;

import java.util.Optional;

/**
 * Represents a counter card, a special kind of {@link Card} that can be played
 * in reaction to an {@link ApplicableCard}.
 *
 * <p>A counter card does not take effect on its own. Instead, it is applied to
 * another card. The result of this interaction is returned to the caller, which allows counter
 * cards to be chained or evaluated by the game logic.</p>
 *
 * @see Card
 * @see ApplicableCard
 */
public interface Counter extends Card {
    /**
     * Returns the Card to apply.
     *
     * @param card the card this counter is applied to; must not be {@code null}
     * @return an {@link Optional} containing the resulting card if the effect
     *         still applies after the counter, or {@link Optional#empty()} if
     *         the given card was completely countered
     * @throws NullPointerException if {@code card} is {@code null}
     */
    Optional<ApplicableCard> apply(ApplicableCard card);
}
