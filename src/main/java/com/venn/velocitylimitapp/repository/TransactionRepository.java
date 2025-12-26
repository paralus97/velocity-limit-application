package com.venn.velocitylimitapp.repository;

import com.venn.velocitylimitapp.model.TransactionAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

/**
 * A data access layer for the in-memory database operations and queries. Stores successful transaction attempts.
 * Queries existing transactions.
 */
@Repository
public interface TransactionRepository extends JpaRepository<TransactionAttempt, String> {


//    boolean existsByIdAndCustomerId();
//
//
//    double sumAmountByCustomerInPeriod(
//            @Param("customerId") String customerId,
//            @Param("startTime") OffsetDateTime startTime,
//            @Param("endTime") OffsetDateTime endTime);
//
//
//
//    long countByCustomerInPeriod(
//            @Param("customerId") String customerId,
//            @Param("startTime") OffsetDateTime startTime,
//            @Param("endTime") OffsetDateTime endTime);
}
