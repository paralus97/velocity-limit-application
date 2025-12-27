package com.venn.velocitylimitapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class TransactionAttempt {
    @JsonProperty("id") private final Long id;

    @JsonProperty("customer_id") private final Long customerId;

    @JsonProperty("load_amount") private final String transactionAmount;

    @JsonProperty("time") private final String time;

    public double getTransactionAmount() {
        return Double.parseDouble(transactionAmount.replace("$", ""));
    }

    @Override
    public String toString() {
        return String.format("{ id = %s, customer_id = %s, load_amount = %s, time = %s}", id, customerId,
                transactionAmount, time);
    }
}