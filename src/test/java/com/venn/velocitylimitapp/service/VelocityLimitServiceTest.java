package com.venn.velocitylimitapp.service;

import com.venn.velocitylimitapp.model.LoadAttempt;
import com.venn.velocitylimitapp.model.LoadResponse;
import com.venn.velocitylimitapp.repository.TransactionEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    private LoadAttempt loadAttempt;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        loadAttempt = LoadAttempt.builder()
                .id(100L)
                .customerId(1L)
                .loadAmount("$500.50")
                .time("2000-01-01T00:00:00Z")
                .build();
    }

    /**
     * An individual customer should receive an empty response if the same transaction ID is submitted twice.
     */
    @Test
    void testDuplicateTransactionIdPerCustomer() {
        // This mockito sets up a situation where we pretend there is already an entry in the database for ta1
        when(repository.existsByIdAndCustomerId(loadAttempt.getId(), loadAttempt.getCustomerId())).thenReturn(true);
        Optional<LoadResponse> resp = velocityLimitService.processLoadAttempt(loadAttempt);
        assertTrue(resp.isEmpty(), "Response should be empty for duplicate customer transaction IDs");
    }

    @Test
    void testValidTransactionAttempt() {
        when(repository.existsByIdAndCustomerId(loadAttempt.getId(), loadAttempt.getCustomerId())).thenReturn(false);
        // Mock DB returning 0 previous loads
        when(repository.countByCustomerInPeriod(any(), any(), any())).thenReturn(0L);
        when(repository.getAcceptedAmountsForCustomerInPeriod(any(), any(), any())).thenReturn(List.of());

        Optional<LoadResponse> response = velocityLimitService.processLoadAttempt(loadAttempt);

        assertTrue(response.isPresent(), "Valid transaction attempt should have a response");
        assertTrue(response.get().getAccepted(), "Valid transaction attempt should have accepted set to true");
        assertEquals("100", response.get().getId(), "Transaction response ID should match transaction attempt ID");

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
        Optional<LoadResponse> response = velocityLimitService.processLoadAttempt(loadAttempt);
        assertTrue(response.isPresent(), "Exceeding daily transaction count limit should get a response");
        assertFalse(response.get().getAccepted(), "Exceeding daily transaction count limit should have accepted set to false");
        // Check that the unacceptable transaction was saved
        verify(repository).save(argThat(t -> !t.isAccepted()));
    }

    @Test
    void testDailyAmountLimit() {
        LoadAttempt.builder()
                .id(100L)
                .customerId(1L)
                .loadAmount("1000.00")
                .time("2000-01-01T00:00:00Z")
                .build();

        when(repository.existsByIdAndCustomerId(loadAttempt.getId(), loadAttempt.getCustomerId()))
                .thenReturn(false);
        when(repository.countByCustomerInPeriod(any(), any(), any())).thenReturn(0L);

        // Scenario where user loaded 4500 today. 4500 + 1000 > $5000 which exceeds daily limit
        when(repository.getAcceptedAmountsForCustomerInPeriod(any(), any(), any()))
                .thenReturn(List.of(new BigDecimal("4500.00")));

        Optional<LoadResponse> response = velocityLimitService.processLoadAttempt(loadAttempt);

        assertTrue(response.isPresent());
        assertFalse(response.get().getAccepted());
    }

    // TODO: implement weekly test. Strategy, mock the first pass of getAcceptedAmountsForCustomerInPeriod for the
    //       daily check, then mock the second pass to check the weekly condition
//    @Test
//    void testWeeklyAmountLimit() {
//        loadAttempt = LoadAttempt.builder()
//                .id(100L)
//                .customerId(1L)
//                .loadAmount("500.00")
//                .time("2000-01-01T00:00:00Z")
//                .build();
//        when(repository.existsByIdAndCustomerId(loadAttempt.getId(), loadAttempt.getCustomerId()))
//                .thenReturn(false);
//        when(repository.countByCustomerInPeriod(any(), any(), any()))
//                .thenReturn(0L);
//
//        when(repository.getAcceptedAmountsForCustomerInPeriod(any(), any(), eq(loadAttempt.getTime().plusDays(1))))
//                .thenReturn(List.of(BigDecimal.ZERO));
//
//        when(repository.getAcceptedAmountsForCustomerInPeriod(any(), any(), any()))
//                .thenReturn(List.of(new BigDecimal("$20000.00")));
//
//        Optional<LoadResponse> response = velocityLimitService.processLoadAttempt(loadAttempt);
//
//        assertTrue(response.isPresent());
//        assertFalse(response.get().getAccepted());
//    }
}
