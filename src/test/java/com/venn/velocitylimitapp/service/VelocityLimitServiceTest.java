package com.venn.velocitylimitapp.service;

import com.venn.velocitylimitapp.model.TransactionAttempt;
import com.venn.velocitylimitapp.model.TransactionResponse;
import com.venn.velocitylimitapp.repository.TransactionEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VelocityLimitServiceTest {

    @Mock
    private TransactionEntityRepository repository;

    @InjectMocks
    private VelocityLimitService velocityLimitService;

    private TransactionAttempt transactionAttempt;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        transactionAttempt = TransactionAttempt.builder()
                .id(100L)
                .customerId(1L)
                .transactionAmount("$500.50")
                .time("2000-01-01T00:00:00Z")
                .build();
    }

    /**
     * An individual customer should receive an empty response if the same transaction ID is submitted twice.
     */
    @Test
    void testDuplicateTransactionIdPerCustomer() {
        // This mockito sets up a situation where we pretend there is already an entry in the database for ta1
        when(repository.existsByIdAndCustomerId(transactionAttempt.getId(), transactionAttempt.getCustomerId())).thenReturn(true);
        Optional<TransactionResponse> resp = velocityLimitService.processTransactionAttempt(transactionAttempt);
        assertTrue(resp.isEmpty(), "Response should be empty for duplicate customer transaction IDs");
    }

    @Test
    void testValidTransactionAttempt() {
        when(repository.existsByIdAndCustomerId(transactionAttempt.getId(), transactionAttempt.getCustomerId())).thenReturn(false);
        // Mock DB returning 0 previous loads
        when(repository.countByCustomerInPeriod(any(), any(), any())).thenReturn(0L);
        when(repository.getAcceptedAmountsForCustomerInPeriod(any(), any(), any())).thenReturn(List.of());

        Optional<TransactionResponse> response = velocityLimitService.processTransactionAttempt(transactionAttempt);

        assertTrue(response.isPresent(), "Valid transaction attempt should have a response");
        assertTrue(response.get().accepted(), "Valid transaction attempt should have accepted set to true");
        assertEquals("100", response.get().id(), "Transaction response ID should match transaction attempt ID");

        // Verify it was saved as accepted
        verify(repository).save(
                argThat(t -> t.isAccepted()
                        && t.getTransactionAmount().equals(new BigDecimal("500.5"))
                )
        );
    }

    @Test
    void testDailyCountLimit() {
        when(repository.existsById("100")).thenReturn(false);
        when(repository.countByCustomerInPeriod(any(), any(), any())).thenReturn(3L);
        Optional<TransactionResponse> response = velocityLimitService.processTransactionAttempt(transactionAttempt);
        assertTrue(response.isPresent(), "Exceeding daily transaction count limit should get a response");
        assertFalse(response.get().accepted(), "Exceeding daily transaction count limit should have accepted set to false");
        // Check that the unacceptable transaction was saved
        verify(repository).save(argThat(t -> !t.isAccepted()));
    }

    @Test
    void testDailyAmountLimit() {
        transactionAttempt.setTransactionAmount("$1000.00"); // Trying to load 1000

        when(repository.existsByIdAndCustomerId(transactionAttempt.getId(), transactionAttempt.getCustomerId()))
                .thenReturn(false);
        when(repository.countByCustomerInPeriod(any(), any(), any())).thenReturn(0L);

        // Scenario where user loaded 4500 today. 4500 + 1000 > $5000 which exceeds daily limit
        when(repository.getAcceptedAmountsForCustomerInPeriod(any(), any(), any()))
                .thenReturn(List.of(new BigDecimal("4500.00")));

        Optional<TransactionResponse> response = velocityLimitService.processTransactionAttempt(transactionAttempt);

        assertTrue(response.isPresent());
        assertFalse(response.get().accepted());
    }

    // TODO: implement weekly test
//    void testWeeklyAmountLimit() {
//        transactionAttempt.setTransactionAmount("1000.00"); // Trying to load 1000
//
//        when(repository.existsByIdAndCustomerId(transactionAttempt.getId(), transactionAttempt.getCustomerId()))
//                .thenReturn(false);
//        when(repository.countByCustomerInPeriod(any(), any(), any())).thenReturn(0L);
//
//
//        when(repository.countByCustomerInPeriod(eq(1L), any(),
//                        eq(LocalDateTime.parse(transactionAttempt.getTime()).plusDays(1L))
//                ).thenReturn(BigDecimal.ZERO));
//        // Scenario where user loaded 4500 today. 4500 + 1000 > $5000 which exceeds daily limit
//        when(repository.returnAcceptedAmountsForCustomerInPeriod(any(), any(), any()))
//                .thenReturn(List.of(new BigDecimal("4500.00")));
//
//        Optional<TransactionResponse> response = velocityLimitService.processTransactionAttempt(transactionAttempt);
//
//        assertTrue(response.isPresent());
//        assertFalse(response.get().accepted());
//
//        Optional<TransactionResponse> response = velocityLimitService.processTransactionAttempt(transactionAttempt);
//
//        assertTrue(response.isPresent());
//        assertFalse(response.get().accepted());
//    }
}
