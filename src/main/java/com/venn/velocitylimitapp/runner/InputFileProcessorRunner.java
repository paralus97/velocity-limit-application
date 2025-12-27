package com.venn.velocitylimitapp.runner;

import com.venn.velocitylimitapp.model.TransactionAttempt;
import com.venn.velocitylimitapp.model.TransactionResponse;
import com.venn.velocitylimitapp.service.VelocityLimitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class InputFileProcessorRunner implements CommandLineRunner {

    @Autowired
    VelocityLimitService velocityLimitService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void run(String... args) throws Exception {

        File inputFile = new File("assets/Venn-Back-End-Input.txt");

        if (inputFile == null) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                try {
                    TransactionAttempt attempt = mapper.readValue(line, TransactionAttempt.class);
                    Optional<TransactionResponse> response = velocityLimitService.processTransactionAttempt(
                            attempt
                    );

                    if (response.isPresent()) {
                        // Write JSON to file
                        String jsonOutput = mapper.writeValueAsString(response.get());
                        writer.write(jsonOutput);
                        writer.newLine(); // Add new line for the next record
                    }
                } catch (Exception e) {
                    log.error("Error processing line: {}", line, e);
                }
            }
        }
    }
}
