package com.venn.velocitylimitapp.controller;

import com.venn.velocitylimitapp.service.VelocityLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@Controller
@RestController
public class VelocityTransactionController {

    @Autowired VelocityLimitService velocityLimitService;
    private final ObjectMapper mapper = new ObjectMapper();

    @GetMapping(value = "/getoutputfromtestinput")
    public List<String> getOutputFromTestInput() throws IOException {
        return List.of();
    }

}
