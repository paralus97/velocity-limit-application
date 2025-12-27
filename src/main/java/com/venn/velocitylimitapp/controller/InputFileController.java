package com.venn.velocitylimitapp.controller;

import com.venn.velocitylimitapp.model.TransactionAttempt;
import com.venn.velocitylimitapp.model.TransactionResponse;
import com.venn.velocitylimitapp.service.VelocityLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.ObjectMapper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Controller
@RestController
public class InputFileController {

    @Autowired VelocityLimitService velocityLimitService;
    private final ObjectMapper mapper = new ObjectMapper();

    @GetMapping(value = "/getoutputfromtestinput")
    public List<String> getOutputFromTestInput() throws IOException {
        List<String> results = new ArrayList<>();

        Path inputPath = Paths.get("/home/perikles/development/velocitylimitapp/assets/input.txt");

        Path file = Paths.get("/home/perikles/development/velocitylimitapp/assets/output.txt");

        try (Stream<String> lines = Files.lines(inputPath)) {
            lines.forEach(line -> {
                try {
                    // To test input, I just read everything into a list and printed it to the response
                    TransactionAttempt attempt = mapper.readValue(line, TransactionAttempt.class);
                    velocityLimitService.processTransactionAttempt(attempt)
                            .ifPresent(response -> {
                                try {
                                    results.add(mapper.writeValueAsString(response));
                                } catch (Exception e) { /* log error */ }
                            });
                } catch (Exception e) { /* log skip malformed line */ }
            });
        }
        Files.write(file, results, StandardCharsets.UTF_8);
        return results;
    }

}
