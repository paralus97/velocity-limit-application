package com.venn.velocitylimitapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;

@Entity
public record TransactionAttempt(
        @Id long id,
        @JsonProperty("customer_id") String customerId,
        @JsonProperty("load_amount") String transactionAmount,
        OffsetDateTime time
) {
    public double getTransactionAmount() {
        return Double.parseDouble(transactionAmount.replace("$", ""));
    }
}