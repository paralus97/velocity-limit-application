package com.venn.velocitylimitapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class TransactionAttempt {

    @Id String id;
    @JsonProperty("customer_id") String customerId;
    @JsonProperty("load_amount") String transactionAmount;
    OffsetDateTime time;

    public double getTransactionAmount() {
        return Double.parseDouble(transactionAmount.replace("$", ""));
    }
}