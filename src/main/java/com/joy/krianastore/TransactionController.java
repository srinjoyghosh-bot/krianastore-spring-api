package com.joy.krianastore;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transaction")
@AllArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionDto>> recordTransaction(@RequestBody TransactionDto transactionDTO,Principal currentUser) {
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
