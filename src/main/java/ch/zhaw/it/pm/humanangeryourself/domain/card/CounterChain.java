package ch.zhaw.it.pm.humanangeryourself.domain.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Remembers which card was played originally and which counters were played in response,
 * and works out which card finally takes effect.
 *
 * <p>Every counter in the chain counters the card played right before it.
 * A counter that is countered itself loses its effect.</p>
 *
 * <p>The last counter of the chain cannot be countered any more and is therefore always active.
 * The first counter is active exactly if the chain has an odd number of counters.</p>
 */
public class CounterChain {
    private final ApplicableCard originalCard;
    private final List<Counter> counters = new ArrayList<>();

    /**
     * Creates a chain for the given card without any counters.
     *
     * @param originalCard the card that was originally played, must not be {@code null}
     * @throws NullPointerException if {@code originalCard} is {@code null}
     */
    public CounterChain(ApplicableCard originalCard) {
        this.originalCard = Objects.requireNonNull(originalCard, "originalCard must not be null") ;
    }

    /**
     * Returns the card that was played originally.
     *
     * @return the original card
     */
    public ApplicableCard getOriginalCard() {
        return originalCard;
    }

    /**
     * Adds a counter that counters the card played right before it
     * (the original card if the chain is still empty, otherwise the last counter).
     *
     * @param counter the counter to add, must not be {@code null}
     * @throws NullPointerException if {@code counter} is {@code null}
     */
    public void add(Counter counter){
        counters.add(Objects.requireNonNull(counter, "counter must not be null"));
    }

    /**
     * Returns the number of counters in the chain.
     *
     * @return the number of counters played so far
     */
    public int size(){
        return counters.size();
    }

    /**
     * Returns whether the last played counter can still be countered by the other side,
     * which is the case as long as at least one counter was played.
     *
     * @return {@code true} if at least one counter has been played
     */
    public boolean hasCounters(){
        return !counters.isEmpty();
    }

    /**
     * Works out which card takes effect after all counters have been played.
     *
     * @return the original card if it was not countered (or the counter was countered itself),
     *         otherwise the result of the first counter, which is empty if the card was blocked
     */
    public Optional<ApplicableCard> resolve(){
        boolean firstCounterIsActive = counters.size() % 2 == 1;
        if(firstCounterIsActive){
            return counters.getFirst().apply(originalCard);
        }
        return Optional.of(originalCard);

    }
}
