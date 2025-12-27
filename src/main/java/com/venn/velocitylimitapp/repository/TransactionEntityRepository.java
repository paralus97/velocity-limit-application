package com.venn.velocitylimitapp.repository;

import com.venn.velocitylimitapp.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * A data access layer for the in-memory database operations and queries. Stores successful transaction attempts.
 * Queries existing transactions.
 */
@Repository
public interface TransactionEntityRepository extends JpaRepository<TransactionEntity, String> {

    /**
     * Return boolean value indicating if the transaction ID exists for a given user ID.
     * @return
     */
    boolean existsByIdAndCustomerId(Long id, Long customerId);

    // Less than end time because new day starts 1 min after
    @Query(value = "SELECT COUNT(t) FROM TransactionEntity t WHERE t.customerId = :customerId AND t.accepted = true AND t.time >= :startTime AND t.time < :endTime")
    long countByCustomerInPeriod(
            @Param("customerId") Long customerId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    // Sum successful load amounts for a customer within a time range
    @Query("SELECT t.transactionAmount FROM TransactionEntity t WHERE t.customerId = :customerId AND t.accepted = true AND t.time >= :startTime AND t.time < :endTime")
    List<BigDecimal> returnAcceptedAmountsForCustomerInPeriod(@Param("customerId") Long customerId,
                                                              @Param("startTime") LocalDateTime startTime,
                                                              @Param("endTime") LocalDateTime endTime);
}
