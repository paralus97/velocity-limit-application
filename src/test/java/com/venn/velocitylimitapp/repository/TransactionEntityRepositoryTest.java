package com.venn.velocitylimitapp.repository;

import com.venn.velocitylimitapp.entity.TransactionEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TransactionEntityRepositoryTest {

    @Autowired
    private TransactionEntityRepository repository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void testGetAcceptedAmountsForCustomerInPeriod() {
        // Given
        LocalDateTime now = LocalDateTime.of(2025, 1, 1, 12, 0);

        // Valid transaction attempt
        TransactionEntity res = repository.save(new TransactionEntity(1L, 100L, new BigDecimal("100.00"), now, true));
        //Valid transaction attempt
        repository.save(new TransactionEntity(2L, 100L, new BigDecimal("50.00"), now.plusHours(1), true));
        // Invalid transaction attempt
        repository.save(new TransactionEntity(3L, 100L, new BigDecimal("1000.00"), now.plusHours(2), false));
        // Different customer ID
        repository.save(new TransactionEntity(4L, 400L, new BigDecimal("100.00"), now, true));

        List<BigDecimal> amounts = repository.getAcceptedAmountsForCustomerInPeriod(
                100L,
                now.minusDays(1),
                now.plusDays(1)
        );
        System.out.println(amounts);
        assertEquals(new BigDecimal("150.00"), amounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    @Test
    void testSumReturnsZeroForEmpty() {
        List<BigDecimal> amounts = repository.getAcceptedAmountsForCustomerInPeriod(
                900L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );

        assertTrue(amounts.isEmpty(), "User ID not in db, should be empty list back");
    }

    @Test
    void testCountAcceptedTransactions() {
        LocalDateTime now = LocalDateTime.now();

        repository.save(new TransactionEntity(1L, 100L, new BigDecimal("10.00"), now, true));
        repository.save(new TransactionEntity(2L, 100L, new BigDecimal("10.00"), now, true));
        repository.save(new TransactionEntity(3L, 100L, new BigDecimal("10.00"), now, false));

        long count = repository.countByCustomerInPeriod(
                100L,
                now.minusHours(1),
                now.plusHours(1)
        );

        assertEquals(2L, count, "Only accepted transactions are counted");
    }
}
