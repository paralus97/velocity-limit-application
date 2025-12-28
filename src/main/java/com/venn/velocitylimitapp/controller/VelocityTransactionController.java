package com.venn.velocitylimitapp.controller;

import com.venn.velocitylimitapp.model.LoadAttempt;
import com.venn.velocitylimitapp.model.LoadResponse;
import com.venn.velocitylimitapp.service.VelocityLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

@Controller
@RestController
public class VelocityTransactionController {

    @Autowired VelocityLimitService velocityLimitService;
    private final ObjectMapper mapper = new ObjectMapper();

    // Currently not working due to generated ID annotation being missing from TransactionEntity.
    // Would try fix it if had more time.
    // Fix it by having generated id for transaction entity? then transaction id (from TransactionAttempt), customer id
    // or embedded id, composite key
    @PostMapping(value = "/create_load_attempt")
    public ResponseEntity<LoadResponse> getOutputFromTestInput(@RequestBody LoadAttempt loadAttempt) {
        Optional<LoadResponse> resp = velocityLimitService.processLoadAttempt(loadAttempt);
        if (resp.isPresent()) {
            return new ResponseEntity<>(resp.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
