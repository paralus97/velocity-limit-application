package com.venn.velocitylimitapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

public record TransactionResponse(
        @Id Long id,
        @JsonProperty("customer_id") Long customerId,
        Boolean accepted
) {
}
