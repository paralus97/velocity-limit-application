package com.venn.velocitylimitapp.runner;

import com.venn.velocitylimitapp.model.LoadAttempt;
import com.venn.velocitylimitapp.model.LoadResponse;
import com.venn.velocitylimitapp.service.VelocityLimitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.util.Optional;

import static com.venn.velocitylimitapp.VelocityLimitApplication.APPLICATION_OUTPUT_PATH;
import static com.venn.velocitylimitapp.VelocityLimitApplication.VENN_BACK_END_INPUT_PATH;

@Component
@RequiredArgsConstructor
@Slf4j
public class InputFileProcessorRunner implements CommandLineRunner {

    @Autowired
    VelocityLimitService velocityLimitService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void run(String... args) throws Exception {

        File inputFile = new File(VENN_BACK_END_INPUT_PATH);

        if (inputFile == null) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(APPLICATION_OUTPUT_PATH))) {
            String line;
            boolean firstIter = true;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                try {
                    LoadAttempt attempt = mapper.readValue(line, LoadAttempt.class);
                    Optional<LoadResponse> response = velocityLimitService.processLoadAttempt(
                            attempt
                    );

                    if (response.isPresent()) {
                        if (!firstIter) {
                            writer.newLine();
                        }
                        // Write JSON to file
                        String jsonOutput = mapper.writeValueAsString(response.get());
                        writer.write(jsonOutput);
                    }
                    if (firstIter) {
                        firstIter = false;
                    }
                } catch (Exception e) {
                    log.error("Error processing line: {}", line, e);
                }
            }
        }
    }
}
