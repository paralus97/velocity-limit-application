package com.venn.velocitylimitapp.service;

import com.venn.velocitylimitapp.entity.TransactionEntity;
import com.venn.velocitylimitapp.model.TransactionAttempt;
import com.venn.velocitylimitapp.model.TransactionResponse;
import com.venn.velocitylimitapp.repository.TransactionEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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
    private static final BigDecimal MAX_DAILY_VELOCITY_LIMIT = BigDecimal.valueOf(5000d);
    private static final BigDecimal MAX_WEEKLY_VELOCITY_LIMIT = BigDecimal.valueOf(20000d);
    private static final Logger log = LoggerFactory.getLogger(VelocityLimitService.class);

    @Autowired
    TransactionEntityRepository transactionEntityRepository;

    public Optional<TransactionResponse> processTransactionAttempt(TransactionAttempt attempt) {

        // If load ID already in database, ignore attempt, return empty optional. No response given.
        // Specification is if ID is seen twice for a user. Not just if seen twice.
        // TODO: Enhance processing to account for ID seen per user and not just
        //       seen in table as per requirements in doc. Perhaps make ID and
        //       customer ID a joint key in the database?
        if (transactionEntityRepository.existsByIdAndCustomerId(attempt.getId(), attempt.getCustomerId())) {
            log.error("DETECTED DUPLICATE ID");
            //System.out.println("id = " + attempt.getId() + ", customerId = " + attempt.getCustomerId());
            return Optional.empty();
        }
        // If here, then dealing with unique transaction

        boolean acceptedTransaction = true;
        BigDecimal transactionAmount = BigDecimal.valueOf(attempt.getTransactionAmount());
        LocalDateTime transactionTime = LocalDateTime.parse(attempt.getTime(), DateTimeFormatter.ISO_DATE_TIME);
        Long customerId = attempt.getCustomerId();

        // Extract customer ID from TransactionAttempt. Use this ID to check load limits as follows:
        //      - A maximum of $5,000 can be loaded per day
        //      - A maximum of $20,000 can be loaded per week
        //      - A maximum of 3 loads can be performed per day, regardless of amount

        // Check load count for day of transaction

        // Get the transaction
        // note the date
        // if this transaction is the 4th in the day, reject

        //Start of day, based off of transaction time
        LocalDateTime startOfDay = transactionTime.truncatedTo(ChronoUnit.DAYS);
        LocalDateTime startOfWeek = transactionTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .truncatedTo(ChronoUnit.DAYS);
        long count = transactionEntityRepository.countByCustomerInPeriod(customerId, startOfDay, transactionTime);
        if (count + 1 > MAX_DAILY_TRANSACTIONS) {
            log.info("TOO MANY LOADS");
            acceptedTransaction = false;
        }

        // Check daily limit of 5000 not hit
        List<BigDecimal> transactionAmounts = transactionEntityRepository.returnAcceptedAmountsForCustomerInPeriod(customerId, startOfDay, transactionTime);
        BigDecimal totalDaily = transactionAmounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalDaily.add(transactionAmount).compareTo(MAX_DAILY_VELOCITY_LIMIT) > 0) {
            log.error("MAX DAILY VELOCITY LIMIT WAS HIT");
            acceptedTransaction = false;

        }


        // Check weekly 20000 limit not hit
        List<BigDecimal> transactionAmountsW = transactionEntityRepository.returnAcceptedAmountsForCustomerInPeriod(customerId, startOfWeek, transactionTime);
        BigDecimal totalWeekly = transactionAmounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalWeekly.add(transactionAmount).compareTo(MAX_WEEKLY_VELOCITY_LIMIT) > 0) {
            log.error("MAX WEEKLY VELOCITY LIMIT WAS HIT");
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


        return Optional.of(new TransactionResponse(attempt.getId(), attempt.getCustomerId(), acceptedTransaction));
    }
}
