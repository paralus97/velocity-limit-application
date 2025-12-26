package com.venn.velocitylimitapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public record TransactionResponse(
        @Id long id,
        @JsonProperty("customer_id") String customerId,
        boolean accepted
) {
}
