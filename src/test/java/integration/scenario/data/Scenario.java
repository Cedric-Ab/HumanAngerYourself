package integration.scenario.data;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public record Scenario(
        String name,
        List<ScenarioStep> steps
) {
    @Override
    public @NotNull String toString() {
        return name;
    }
}
