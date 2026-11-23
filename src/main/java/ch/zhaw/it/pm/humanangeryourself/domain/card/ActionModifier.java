package ch.zhaw.it.pm.humanangeryourself.domain.card;

import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.config.GamblingConfig;

import java.util.List;
import java.util.stream.Stream;

public sealed interface ActionModifier {
    Action forcedAction();

    non-sealed interface GambleModifier extends ActionModifier {
        /**
         * Returns the gambling configuration for this modifier.
         *
         * @param currentConfig the current gambling configuration
         * @return the gambling configuration
         */
        GamblingConfig getGamblingConfig(GamblingConfig currentConfig);
    }

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
