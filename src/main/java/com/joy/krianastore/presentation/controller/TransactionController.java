package com.joy.krianastore.presentation.controller;

import com.joy.krianastore.core.exception.RateLimitExceededException;
import com.joy.krianastore.domain.services.RateLimitingService;
import com.joy.krianastore.domain.services.TransactionService;
import com.joy.krianastore.domain.dto.ApiResponse;
import com.joy.krianastore.domain.dto.TransactionDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@AllArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final RateLimitingService rateLimitingService;

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionDto>> recordTransaction(@RequestBody TransactionDto transactionDTO, Principal currentUser) {
        if (rateLimitingService.allowRequest("/api/transactions")) {
            throw new RateLimitExceededException("You have exceeded the allowed number of requests. Please try again later.");
        }
        var response = transactionService.recordTransaction(transactionDTO, currentUser);
        return new ResponseEntity<>(new ApiResponse<>(true, "Transaction added!", response), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionDto>>> getTransactions(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate, Principal connectedUser) {
        var response = transactionService.getTransactionsBetweenDates(connectedUser, startDate, endDate);
        return new ResponseEntity<>(new ApiResponse<>(true, "Transactions found!", response), HttpStatus.OK);
    }
}
