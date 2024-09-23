package com.joy.krianastore.domain.services;

import com.joy.krianastore.data.models.Transaction;
import com.joy.krianastore.data.dao.TransactionRepository;
import com.joy.krianastore.data.models.User;
import com.joy.krianastore.domain.dto.ReportDto;
import lombok.AllArgsConstructor;
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
public class ReportsService {
    private final TransactionRepository transactionRepository;

    public ReportDto generateWeeklyReport(Principal connectedUser) {
        LocalDate endDate = LocalDate.now().plusDays(1);
        LocalDate startDate = endDate.minusDays(7);
        return generateReport(startDate, endDate, connectedUser);
    }

    public ReportDto generateMonthlyReport(Principal connectedUser) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        return generateReport(startDate, endDate, connectedUser);
    }

    public ReportDto generateYearlyReport(Principal connectedUser) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfYear(1);
        LocalDate endDate = now.withDayOfYear(now.lengthOfYear());
        return generateReport(startDate, endDate, connectedUser);
    }

    private ReportDto generateReport(LocalDate startDate, LocalDate endDate, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        List<Transaction> transactions = transactionRepository.findAllByStoreIdAndTransactionDateBetween(user.getStore().getId(), startDate, endDate);

        BigDecimal totalCredits = transactions.stream()
                .filter(Transaction::isCredit)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDebits = transactions.stream()
                .filter(transaction -> !transaction.isCredit())
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netFlow = totalCredits.subtract(totalDebits);

        Map<String, BigDecimal> dailyBreakdown = transactions.stream()
                .collect(Collectors.groupingBy(tr -> tr.getTransactionDate().toString(),
                        Collectors.reducing(BigDecimal.ZERO, txn -> txn.isCredit() ? txn.getAmount() : txn.getAmount().negate(), BigDecimal::add)));

        return new ReportDto(totalCredits, totalDebits, netFlow, dailyBreakdown);
    }
}
