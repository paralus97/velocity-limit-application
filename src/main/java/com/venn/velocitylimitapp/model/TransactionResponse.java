package com.venn.velocitylimitapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TransactionResponse(
        String id,
        @JsonProperty("customer_id") String customerId,
        Boolean accepted
) {
}
