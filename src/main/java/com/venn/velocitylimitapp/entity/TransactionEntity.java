package com.venn.velocitylimitapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data // is this needed?
@Table(name = "transaction_entities")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {
    @Id private Long id;
    private Long customerId;
    private BigDecimal transactionAmount;
    private LocalDateTime time;
    private boolean accepted;
}
