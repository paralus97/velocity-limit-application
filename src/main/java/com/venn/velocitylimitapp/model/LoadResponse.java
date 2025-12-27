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

    @JsonProperty("id") private final String id;

    @JsonProperty("customer_id") private final String customerId;

    @JsonProperty("accepted") private final Boolean accepted;

}
