package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

public interface HasPreconditions extends Card {
    // separate interface or with true as default value in Card
    boolean isApplyable(Game game, Player player);
}
