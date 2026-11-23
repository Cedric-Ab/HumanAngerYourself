package integration.scenario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import integration.scenario.data.Scenario;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public final class ScenarioLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory()).findAndRegisterModules();

    private ScenarioLoader() {
    }

    public static Scenario load(Path path) {
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            throw new IllegalArgumentException("File does not exist or is not a regular file: " + path);
        }

        try {
            return MAPPER.readValue(path.toFile(), Scenario.class);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load scenario: " + path, e);
        }
    }

    public static Scenario load(InputStream stream) {
        try {
            return MAPPER.readValue(stream, Scenario.class);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load scenario", e);
        }
    }

    public static Scenario loadResource(String resourcePath) {
        try (InputStream stream = ScenarioLoader.class.getResourceAsStream(resourcePath)) {

            if (stream == null) {
                throw new IllegalArgumentException("Scenario resource not found: " + resourcePath);
            }

            return load(stream);

        } catch (IOException e) {
            throw new IllegalStateException("Failed to load scenario resource: " + resourcePath, e);
        }
    }

    public static List<Scenario> loadAll(Path directory) {
        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            throw new IllegalArgumentException("Directory does not exist or is not a directory: " + directory);
        }

        try (Stream<Path> files = Files.walk(directory)) {
            return files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".yaml") || path.toString().endsWith(".yml"))
                    .sorted()
                    .map(ScenarioLoader::load)
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load scenarios from " + directory, e);
        }
    }
}
