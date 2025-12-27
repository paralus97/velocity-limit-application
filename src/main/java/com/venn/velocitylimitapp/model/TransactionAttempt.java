package com.venn.velocitylimitapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAttempt {
    //TODO: See about making these immutable
    @JsonProperty("id") private Long id;

    @JsonProperty("customer_id") private Long customerId;

    @JsonProperty("load_amount") private String transactionAmount;

    @JsonProperty("time") private String time;

    public double getTransactionAmount() {
        return Double.parseDouble(transactionAmount.replace("$", ""));
    }

    @Override
    public String toString() {
        return String.format("{ id = %s, customer_id = %s, load_amount = %s, time = %s}", id, customerId,
                transactionAmount, time);
    }
}