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
     * @return True if the row was previously seen. Other-wise false.
     */
    boolean existsByIdAndCustomerId(Long id, Long customerId);

    /**
     * Count the number of unique and accepted transactions for a given customer.
     * @param customerId The id of the customer.
     * @param startTime The beginning of the time range to check.
     * @param endTime The end of the time range to check.
     * @return The count of TransactionEntities within the time interval.
     */
    @Query(value = "SELECT COUNT(t) FROM TransactionEntity t WHERE t.customerId = :customerId " +
                   "AND t.accepted = true AND t.time >= :startTime AND t.time < :endTime")
    long countByCustomerInPeriod(
            @Param("customerId") Long customerId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * Return a list of the amounts given in accepted TransactionEntities for a given customer and time interval.
     * @param customerId The id of the customer.
     * @param startTime The beginning of the time range to check.
     * @param endTime The end of the time range to check.
     * @return The list of transaction amounts within the time interval.
     */
    @Query(value = "SELECT t.transactionAmount FROM TransactionEntity t WHERE t.customerId = :customerId " +
                   "AND t.accepted = true AND t.time >= :startTime AND t.time < :endTime")
    List<BigDecimal> getAcceptedAmountsForCustomerInPeriod(@Param("customerId") Long customerId,
                                                              @Param("startTime") LocalDateTime startTime,
                                                              @Param("endTime") LocalDateTime endTime);
}
