package com.venn.velocitylimitapp.repository;

import com.venn.velocitylimitapp.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A data access layer for the in-memory database operations and queries. Stores successful transaction attempts.
 * Queries existing transactions.
 */
@Repository
public interface TransactionEntityRepository extends JpaRepository<TransactionEntity, String> {


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
