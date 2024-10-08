package com.joy.krianastore.domain.services;

import com.joy.krianastore.data.models.Transaction;
import com.joy.krianastore.data.dao.TransactionRepository;
import com.joy.krianastore.data.models.User;
import com.joy.krianastore.domain.dto.ReportDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ReportsService {
    private final TransactionRepository transactionRepository;

    /**
     * Generates weekly report
     * @param connectedUser is the currently logged-in user
     * @return the weekly report for the store of the user
     */
    public ReportDto generateWeeklyReport(Principal connectedUser) {
        log.info("Generating weekly report...");
        LocalDate endDate = LocalDate.now().plusDays(1);
        LocalDate startDate = endDate.minusDays(7);
        return generateReport(startDate, endDate, connectedUser);
    }

    /**
     * Generates monthly report
     * @param connectedUser is the currently logged-in user
     * @return the monthly report for the store of the user
     */
    public ReportDto generateMonthlyReport(Principal connectedUser) {
        log.info("Generating monthly report...");
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        return generateReport(startDate, endDate, connectedUser);
    }

    /**
     * Generates yearly report
     * @param connectedUser is the currently logged-in user
     * @return the yearly report for the store of the user
     */
    public ReportDto generateYearlyReport(Principal connectedUser) {
        log.info("Generating yearly report...");
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfYear(1);
        LocalDate endDate = now.withDayOfYear(now.lengthOfYear());
        return generateReport(startDate, endDate, connectedUser);
    }

    /**
     * Generates report between for any period
     * @param connectedUser is the currently logged-in user
     * @param startDate is the beginning date for the period
     * @param endDate is the end date for the period
     * @return the report for the store of the user for the given period
     */
    private ReportDto generateReport(LocalDate startDate, LocalDate endDate, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        log.info("Fetching transactions between {} and {}", startDate, endDate);
        List<Transaction> transactions = transactionRepository.findAllByStoreIdAndTransactionDateBetween(user.getStore().getId(), startDate, endDate);
        log.info("Calculation total credits");
        BigDecimal totalCredits = transactions.stream()
                .filter(Transaction::isCredit)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("Calculation total debits");
        BigDecimal totalDebits = transactions.stream()
                .filter(transaction -> !transaction.isCredit())
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("Calculation netFlow");
        BigDecimal netFlow = totalCredits.subtract(totalDebits);
        log.info("Calculation daily breakdown");
        Map<String, BigDecimal> dailyBreakdown = transactions.stream()
                .collect(Collectors.groupingBy(tr -> tr.getTransactionDate().toString(),
                        Collectors.reducing(BigDecimal.ZERO, txn -> txn.isCredit() ? txn.getAmount() : txn.getAmount().negate(), BigDecimal::add)));
        log.info("Generated report for {}", user);
        return new ReportDto(totalCredits, totalDebits, netFlow, dailyBreakdown);
    }
}
