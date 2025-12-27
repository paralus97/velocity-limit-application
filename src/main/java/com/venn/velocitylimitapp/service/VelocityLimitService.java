package com.venn.velocitylimitapp.service;

import com.venn.velocitylimitapp.entity.TransactionEntity;
import com.venn.velocitylimitapp.model.LoadAttempt;
import com.venn.velocitylimitapp.model.LoadResponse;
import com.venn.velocitylimitapp.repository.TransactionEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

/**
 * This class will perform the heavy lifting of the application. It will be invoked per line read of the input file.
 */
@Service
public class VelocityLimitService {

    private static final long MAX_DAILY_TRANSACTIONS = 3;
    private static final BigDecimal MAX_DAILY_VELOCITY_LIMIT = BigDecimal.valueOf(5000.0);
    private static final BigDecimal MAX_WEEKLY_VELOCITY_LIMIT = BigDecimal.valueOf(20000.0);
    private static final Logger log = LoggerFactory.getLogger(VelocityLimitService.class);

    @Autowired
    TransactionEntityRepository transactionEntityRepository;

    public Optional<LoadResponse> processLoadAttempt(LoadAttempt attempt) {
        // If load ID already in database, ignore attempt, return empty optional. No response given.
        // Specification is if ID is seen twice for a user. Not just if seen twice.
        if (transactionEntityRepository.existsByIdAndCustomerId(attempt.getId(), attempt.getCustomerId())) {
            log.debug("id = " + attempt.getId() + ", customerId = " + attempt.getCustomerId());
            return Optional.empty();
        }
        // Assume valid attempt to begin with. Deserialize data
        boolean acceptedTransaction = true;
        BigDecimal transactionAmount = BigDecimal.valueOf(attempt.getTransactionAmount());
        LocalDateTime transactionTime = LocalDateTime.parse(attempt.getTime(), DateTimeFormatter.ISO_DATE_TIME);
        Long customerId = attempt.getCustomerId();
        Long id = attempt.getId();

        // Start of day, based off of transaction time
        if (checkIfDailyTransactionLimitReached(customerId, transactionTime)) {
            log.debug("Daily transaction limit exceeded by transactionId = {} for customerId = {}", id, customerId);
            acceptedTransaction = false;
        }

        // Check daily limit of 5000 not hit
        if (checkIfDailyVelocityLimitReached(customerId, transactionTime, transactionAmount)) {
            log.debug("Daily velocity limit exceeded by transactionId = {} for customerId = {}", id, customerId);
            acceptedTransaction = false;

        }

        // Check weekly 20000 limit not hit
        if (checkIfWeeklyVelocityLimitReached(customerId, transactionTime, transactionAmount)) {
            log.debug("Weekly velocity limit exceeded by transactionId = {} for customerId = {}", id, customerId);
            acceptedTransaction = false;
        }

        // Create TransactionEntity
        transactionEntityRepository.save(TransactionEntity.builder()
                .id(attempt.getId())
                .customerId(attempt.getCustomerId())
                .transactionAmount(transactionAmount)
                .time(transactionTime)
                .accepted(acceptedTransaction)
                .build()
        );

        return Optional.of(new LoadResponse(
                attempt.getId().toString(),
                attempt.getCustomerId().toString(),
                acceptedTransaction
        ));
    }

    private boolean checkIfDailyTransactionLimitReached(Long customerId, LocalDateTime transactionTime) {
        LocalDateTime startOfDay = transactionTime.truncatedTo(ChronoUnit.DAYS);
        long count = transactionEntityRepository.countByCustomerInPeriod(customerId, startOfDay, transactionTime);
        return (count + 1 > MAX_DAILY_TRANSACTIONS);
    }

    private boolean checkIfDailyVelocityLimitReached(Long customerId, LocalDateTime transactionTime, BigDecimal transactionAmount) {
        LocalDateTime startOfDay = transactionTime.truncatedTo(ChronoUnit.DAYS);
        List<BigDecimal> transactionAmounts = transactionEntityRepository.getAcceptedAmountsForCustomerInPeriod(customerId, startOfDay, transactionTime);
        BigDecimal totalDaily = transactionAmounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return (totalDaily.add(transactionAmount).compareTo(MAX_DAILY_VELOCITY_LIMIT) > 0);
    }

    private boolean checkIfWeeklyVelocityLimitReached(Long customerId, LocalDateTime transactionTime, BigDecimal transactionAmount) {
        LocalDateTime startOfWeek = transactionTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .truncatedTo(ChronoUnit.DAYS);
        List<BigDecimal> transactionAmounts = transactionEntityRepository.getAcceptedAmountsForCustomerInPeriod(customerId, startOfWeek, transactionTime);
        BigDecimal totalWeekly = transactionAmounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return (totalWeekly.add(transactionAmount).compareTo(MAX_WEEKLY_VELOCITY_LIMIT) > 0);
    }
}
