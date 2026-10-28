package ch.zhaw.it.pm.humanangeryourself.domain.rules;

import ch.zhaw.it.pm.humanangeryourself.api.Action;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

public class CardEffects {

    public boolean canPlay(Player player, Action action) {
        return false;
    }

    public void apply() {
    }

    public void applyInstant() {
    }

    public void applyHandCard() {
    }
}
