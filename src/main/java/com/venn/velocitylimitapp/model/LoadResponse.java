package com.venn.velocitylimitapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Data class for process load responses/outcomes as LoadResponse.
 */
@Data
@AllArgsConstructor
@Builder
public class LoadResponse {

    /**
     * The id of the transaction received.
     */
    @JsonProperty("id") private final String id;

    /**
     * The customer id associated with the transaction.
     */
    @JsonProperty("customer_id") private final String customerId;

    /**
     * Whether the transaction made was valid/accepted.
     */
    @JsonProperty("accepted") private final Boolean accepted;

}
