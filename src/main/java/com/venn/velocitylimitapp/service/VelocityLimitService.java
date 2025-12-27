package com.venn.velocitylimitapp.service;

import com.venn.velocitylimitapp.model.TransactionAttempt;
import com.venn.velocitylimitapp.model.TransactionResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This class will perform the heavy lifting of the application. It will be invoked per line read of the input file.
 */
@Service
public class VelocityLimitService {

    public Optional<TransactionResponse> processTransactionAttempt(TransactionAttempt attempt) {
        attempt.getTransactionAmount();
        return Optional.of(new TransactionResponse(attempt.getId(), attempt.getCustomerId(), true));
    }
}
