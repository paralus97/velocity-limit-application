package com.venn.velocitylimitapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class for modeling the TransactionEntity object in the database.
 */
@Entity
@Table(name = "transaction_entities")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

    /**
     * The id of the transaction received.
     */
    @Id private Long id;

    /**
     * The customer id associated with the transaction.
     */
    private Long customerId;

    /**
     * The amount in the given transaction, stored as BigDecimal.
     */
    @Getter private BigDecimal transactionAmount;

    /**
     * The time that the transaction was made.
     */
    private LocalDateTime time;

    /**
     * Whether the transaction made was valid/accepted.
     */
    @Getter private boolean accepted;
}
