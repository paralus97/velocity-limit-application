package com.venn.velocitylimitapp.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * See note in TransactionEntity class for more details.
 */
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntityId {
    /**
     * The id of the transaction received.
     */
    private Long id;

    /**
     * The customer id associated with the transaction.
     */
    private Long customerId;
}
