package integration.scenario.data;

import java.util.Optional;

public record ScenarioStep(
        StepInput input,
        Optional<Expectations> expect
) {
}
