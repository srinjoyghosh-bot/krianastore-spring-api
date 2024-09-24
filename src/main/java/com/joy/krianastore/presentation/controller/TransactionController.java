package com.joy.krianastore.presentation.controller;

import com.joy.krianastore.core.exception.RateLimitExceededException;
import com.joy.krianastore.data.models.User;
import com.joy.krianastore.domain.services.RateLimitingService;
import com.joy.krianastore.domain.services.TransactionService;
import com.joy.krianastore.domain.dto.ApiResponse;
import com.joy.krianastore.domain.dto.TransactionDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
     * @param connectedUser is the currently logged-in user
     * @return the saved transaction
     * @throws RateLimitExceededException if the rate limit is exceeded for the endpoint
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TransactionDto>> recordTransaction(@RequestBody TransactionDto transactionDTO, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (!rateLimitingService.allowRequest("/api/transaction", user.getId())) {
            log.error("Rate limit exceeded for /api/transactions for user {}", user.getId());
            throw new RateLimitExceededException("You have exceeded the allowed number of requests. Please try again later.");
        }
        var response = transactionService.recordTransaction(transactionDTO, connectedUser);
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
