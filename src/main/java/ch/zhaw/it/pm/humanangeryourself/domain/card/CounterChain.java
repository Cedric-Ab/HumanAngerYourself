package ch.zhaw.it.pm.humanangeryourself.domain.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CounterChain {
    private final ApplicableCard originalCard;
    private final List<Counter> counters = new ArrayList<>();

    public CounterChain(ApplicableCard originalCard) {
        this.originalCard = originalCard;
    }

    public ApplicableCard getOriginalCard() {
        return originalCard;
    }

    public void add(Counter counter){
        counters.add(counter);
    }

    public int size(){
        return counters.size();
    }

    public boolean hasCounters(){
        return !counters.isEmpty();
    }

    public Optional<ApplicableCard> resolve(){
        boolean firstCounterIsActive = counters.size() % 2 == 1;
        if(firstCounterIsActive){
            return counters.getFirst().apply(originalCard);
        }
        return Optional.of(originalCard);

    }
}
