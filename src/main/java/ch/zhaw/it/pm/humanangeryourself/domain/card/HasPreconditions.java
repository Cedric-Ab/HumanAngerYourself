package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

public interface HasPreconditions extends Card {
    boolean isApplyable(Game game, Player player);

    default void assertApplyable(Game game, Player player) {
        if (!isApplyable(game, player)) {
            throw new IllegalStateException("Card cannot be applied because preconditions are not met.");
        }
    }
}
