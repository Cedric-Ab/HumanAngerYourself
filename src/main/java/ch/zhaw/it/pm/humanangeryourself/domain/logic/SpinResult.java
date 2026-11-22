package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;

public class SpinResult {
    private final Long amount;
    private final CardType cardType;

    public SpinResult(Long amount) {
        this.amount = amount;
        this.cardType = null;
    }
    public SpinResult(CardType cardType) {
        this.amount = null;
        this.cardType = cardType;
    }

    public boolean isCard(){
        return cardType != null;
    }

    public Long getAmount() {
        return amount;
    }

    public CardType getCardType() {
        return cardType;
    }
}
