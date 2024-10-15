package pl.zajavka.mortgage.services;

import pl.zajavka.mortgage.model.InputData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InputDataService {

    private static final Path FILE_LOCATION = Paths.get("src/main/resources/inputData.csv");

    public InputData read() throws IOException {
        var content = Files.readString(FILE_LOCATION)
                .lines()
                .collect(Collectors.groupingBy(line -> line.split(";")[0]));

        validate(content);

        return null;
    }

    private static void validate(final Map<String, List<String>> content) {
        if (content.values().stream().anyMatch(values -> values.size() != 1)) {
            throw new IllegalArgumentException("Configuration mismatch");
        }
    }
}
