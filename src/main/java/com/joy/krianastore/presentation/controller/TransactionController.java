package com.joy.krianastore.presentation.controller;

import com.joy.krianastore.core.exception.RateLimitExceededException;
import com.joy.krianastore.domain.services.RateLimitingService;
import com.joy.krianastore.domain.services.TransactionService;
import com.joy.krianastore.domain.dto.ApiResponse;
import com.joy.krianastore.domain.dto.TransactionDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
/**
 * REST controller for managing transactions.
 * Provides endpoints to record and fetch transactions.
 */
@RestController
@RequestMapping("/api/transaction")
@AllArgsConstructor
@Slf4j
public class TransactionController {
    private final TransactionService transactionService;
    private final RateLimitingService rateLimitingService;

    /**
     * Records a new transaction
     * @param transactionDTO is the transaction details
     * @param currentUser is the currently logged-in user
     * @return the saved transaction
     * @throws RateLimitExceededException if the rate limit is exceeded for the endpoint
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TransactionDto>> recordTransaction(@RequestBody TransactionDto transactionDTO, Principal currentUser) {
        if (rateLimitingService.allowRequest("/api/transactions")) {
            log.error("Rate limit exceeded for /api/transactions");
            throw new RateLimitExceededException("You have exceeded the allowed number of requests. Please try again later.");
        }
        var response = transactionService.recordTransaction(transactionDTO, currentUser);
        return new ResponseEntity<>(new ApiResponse<>(true, "Transaction added!", response), HttpStatus.CREATED);
    }

    /**
     * Fetches the transactions associated with the user's store in the mentioned period
     * @param startDate is the beginning date of the period
     * @param endDate is the end date of the period
     * @param connectedUser is the currently logged-in user
     * @return the list of required transactions
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionDto>>> getTransactions(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate, Principal connectedUser) {
        var response = transactionService.getTransactionsBetweenDates(connectedUser, startDate, endDate);
        return new ResponseEntity<>(new ApiResponse<>(true, "Transactions found!", response), HttpStatus.OK);
    }
}
