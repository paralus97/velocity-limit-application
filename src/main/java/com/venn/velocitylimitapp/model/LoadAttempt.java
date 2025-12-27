package com.venn.velocitylimitapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Data class for processing load attempts as a LoadAttempt.
 */
@Data
@Builder
@AllArgsConstructor
public class LoadAttempt {

    /**
     * The id of the load attempt, used when creating the TransactionEntity object.
     */
    @JsonProperty("id") private final Long id;

    /**
     * The customer id associated with the load.
     */
    @JsonProperty("customer_id") private final Long customerId;

    /**
     * The amount in the given load/transaction, stored as BigDecimal.
     */
    @JsonProperty("load_amount") private final String loadAmount;

    /**
     * The time that the load attempt was made.
     */
    @JsonProperty("time") private final String time;

    /**
     * Process the transactionAmount String by removing thr dollar symbol.
     * @return The transaction amount as a double value.
     */
    public double getTransactionAmount() {
        return Double.parseDouble(loadAmount.replace("$", ""));
    }

    @Override
    public String toString() {
        return String.format("{ id = %s, customer_id = %s, load_amount = %s, time = %s}", id, customerId,
                loadAmount, time);
    }
}