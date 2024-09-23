package com.joy.krianastore;

import java.time.LocalDate;
import java.util.Date;

public class TransactionMapper {

    public static TransactionDto toDTO(Transaction transaction) {
        return new TransactionDto(transaction.getCurrency(),transaction.getAmount(),transaction.isCredit());
    }

    public static Transaction toEntity(TransactionDto dto) {
        Transaction transaction = new Transaction();
        transaction.setCurrency(dto.currency());
        transaction.setAmount(dto.amount());
        transaction.setTransactionDate(LocalDate.now());
        transaction.setCredit(dto.isCredit());
        return transaction;
    }
}
