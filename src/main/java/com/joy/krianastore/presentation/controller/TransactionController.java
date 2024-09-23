package com.joy.krianastore.presentation.controller;

import com.joy.krianastore.domain.services.TransactionService;
import com.joy.krianastore.presentation.dto.ApiResponse;
import com.joy.krianastore.presentation.dto.TransactionDto;
import com.joy.krianastore.utils.TransactionMapper;
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

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionDto>> recordTransaction(@RequestBody TransactionDto transactionDTO, Principal currentUser) {
        var response= transactionService.recordTransaction(transactionDTO,currentUser);
        return new ResponseEntity<>(new ApiResponse<>(true,"Transaction added!",response), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionDto>>> getTransactions(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate, Principal connectedUser) {
        var response= transactionService.getTransactionsBetweenDates(connectedUser,startDate,endDate).stream()
                .map(TransactionMapper::toDTO)
                .toList();
        return new ResponseEntity<>(new ApiResponse<>(true,"Transactions found!",response), HttpStatus.OK);
    }
}
