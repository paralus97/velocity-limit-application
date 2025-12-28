package com.venn.velocitylimitapp.entity;

import jakarta.persistence.*;
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
     * The id of the transaction received from load attempt.
     * NOTE: Because this id may not be completely unique, there is scope to use an embeddedId here
     * made up of a combined key of the Transaction Attempt ID and the Customer ID. The combination
     * of these two is treated as unique in the assignment specification. That, or the entity itself
     * should have a unique generated ID. This would be a better model to ensure db integrity.
     */
    @Id
    private Long id;

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
