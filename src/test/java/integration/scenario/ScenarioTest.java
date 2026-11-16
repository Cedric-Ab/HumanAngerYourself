package integration.scenario;

import integration.scenario.data.Scenario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Path;
import java.util.stream.Stream;

@DisplayName("Game Scenario Tests")
public class ScenarioTest {
    static Stream<Named<Scenario>> allScenarios() {
        return ScenarioLoader
                .loadAll(Path.of("src/test/resources/scenarios"))
                .stream()
                .map(scenario -> Named.of(
                        scenario.name(),
                        scenario
                ));
    }

    @ParameterizedTest
    @MethodSource("allScenarios")
    void scenarioTest(Scenario scenario) {
        new ScenarioRunner()
                .run(scenario);
    }
}
