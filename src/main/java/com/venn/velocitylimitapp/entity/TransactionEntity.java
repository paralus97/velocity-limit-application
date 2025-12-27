package com.venn.velocitylimitapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_entities")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {
    @Id private Long id;


    private Long customerId;

    @Getter private BigDecimal transactionAmount;

    private LocalDateTime time;

    @Getter private boolean accepted;
}
