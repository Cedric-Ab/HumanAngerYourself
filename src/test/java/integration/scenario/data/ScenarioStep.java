package integration.scenario.data;

public record ScenarioStep(
        StepInput input,
        Expectations expect
) {
}
