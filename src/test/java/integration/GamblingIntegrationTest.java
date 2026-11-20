package integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

@DisplayName("Gambling Integration Test")
public class GamblingIntegrationTest {
    // TODO

    @Nested
    @DisplayName("Monetary Reward")
    class MonetaryReward {
//         @DisplayName("Correspondig Gambling Outcome is present")

//         @DisplayName("Money of Player is changed")
    }

    @Disabled
    @Nested
    @DisplayName("Hand Card as Reward")
    class HandCard {
//         @DisplayName("Correspondig Gambling Outcome is present")

//         @DisplayName("Hand Card is added to Players Hand Cards")

//         @DisplayName("Exceding Hand Limit triggers Hand Card Discard")
    }

    @Disabled
    @Nested
    @DisplayName("Instant Card as Reward")
    class InstantCard {
//         @DisplayName("Correspondig Gambling Outcome is present")

//         @DisplayName("Instant Card is triggered")
    }
}
