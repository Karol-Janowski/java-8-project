package pl.zajavka.mortgage.services;

import pl.zajavka.mortgage.model.InputData;
import pl.zajavka.mortgage.model.MortgageType;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InputDataService {

    private static final Path FILE_LOCATION = Paths.get("src/main/resources/inputData.csv");

    public InputData read() throws IOException {
        var content = Files.readString(FILE_LOCATION)
                .lines()
                .collect(Collectors.groupingBy(line -> line.split(";")[0]));

        validate(content);

        var inputData = content.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get(0).split(";")[1]));

        return  new InputData()
                .withRepaymentStartDate(
                        Optional.ofNullable(inputData.get("repaymentStartDate")).map(LocalDate::parse).orElseThrow())
                .withWiborPercent(
                        Optional.ofNullable(inputData.get("wibor")).map(BigDecimal::new).orElseThrow())
                .withAmount(
                        Optional.ofNullable(inputData.get("amount")).map(BigDecimal::new).orElseThrow())
                .withMonthsDuration(
                        Optional.ofNullable(inputData.get("monthsDuration")).map(BigDecimal::new).orElseThrow())
                .withType(
                        Optional.ofNullable(inputData.get("rateType")).map(MortgageType::valueOf).orElseThrow())
                .withMarginPercent(
                        Optional.ofNullable(inputData.get("margin")).map(BigDecimal::new).orElseThrow())
                .withOverpaymentProvisionPercent(
                        Optional.ofNullable("overpaymentProvision").map(BigDecimal::new).orElseThrow())
                .withOverpaymentProvisionMonths(
                        Optional.ofNullable("overpaymentProvisionMonths").map(BigDecimal::new).orElseThrow())
                .withOverpaymentStartMonth(
                        Optional.ofNullable("overpaymentStartMonth").map(BigDecimal::new).orElseThrow())
                .withOverpaymentSchema(
                        Optional.ofNullable("overpaymentSchema").map(this::calculateSchema).orElseThrow())
                .withOverpaymentReduceWay(
                        Optional.ofNullable(inputData.get("overpaymentReduceWay")).orElseThrow())
                .withMortgagePrintPayoffsSchedule(
                        Optional.ofNullable(inputData.get("mortgagePrintPayoffSchedule")).map(Boolean::parseBoolean).orElseThrow())
                .withMortgageRateNumberToPrint(
                        Optional.ofNullable(inputData.get("MortgageRateNumberToPrint")).map(Integer::parseInt).orElseThrow());

    }

    private Map<Integer, BigDecimal> calculateSchema(String s) {
    }

    private static void validate(final Map<String, List<String>> content) {
        if (content.values().stream().anyMatch(values -> values.size() != 1)) {
            throw new IllegalArgumentException("Configuration mismatch");
        }
    }
}
