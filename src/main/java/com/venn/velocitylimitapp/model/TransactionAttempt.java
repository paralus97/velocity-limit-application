package com.venn.velocitylimitapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;


@Data
public class TransactionAttempt {
    //TODO: See about making these immutable
    @JsonProperty("id") private Long id;

    @JsonProperty("customer_id") private Long customerId;

    // TODO: Revisit this. it would be nice to be able to parse this as a double immeadiately. Its proving difficult
    //       to parse it cleanly with it arriving as "$x.yz". For now, lean on getTransactionAmount. Could use a custom
    //       Jackson Deserializer to parse double?
    @JsonProperty("load_amount") private String transactionAmount;

    @JsonProperty("time") private ZonedDateTime time;

    public double getTransactionAmount() {
        return Double.parseDouble(transactionAmount.replace("$", ""));
    }

    @Override
    public String toString() {
        return String.format("{ id = %s, customer_id = %s, load_amount = %s, time = %s}", id, customerId,
                transactionAmount, time);
    }
}