package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.config.GamblingConfig;

import java.util.List;
import java.util.stream.Stream;

/**
 * Represents a modifier that can be applied to an action in the game.
 */
public sealed interface ActionModifier extends Card {
    /**
     * Returns the action that is forced to be executed next by this modifier.
     *
     * @return the action that is forced by this modifier
     */
    Action forcedAction();

    /**
     * Represents a modifier that can be applied to an action in the game that involves gambling.
     */
    non-sealed interface GambleModifier extends ActionModifier {
        /**
         * Returns the gambling configuration for this modifier.
         *
         * @param currentConfig the current gambling configuration
         * @return the gambling configuration
         */
        GamblingConfig getGamblingConfig(GamblingConfig currentConfig);
    }

    /**
     * Represents a modifier that can be applied to an action in the game that involves rolling a die.
     */
    non-sealed interface RollModifier extends ActionModifier {
        /**
         * Returns the possible values that can be rolled with this modifier.
         *
         * @return the possible values that can be rolled with this modifier
         */
        default List<Integer> possibleValues() {
            return Stream.of(1, 2, 3, 4, 5, 6)
                    .map(i -> i * factor())
                    .toList();
        }

        /**
         * The factor by which the rolled value is multiplied.
         * <p>
         * For example, a factor of 2 means that a roll of 1-6 will be treated as 2-12.
         *
         * @return the factor by which the rolled value is multiplied
         */
        int factor();
    }
}
