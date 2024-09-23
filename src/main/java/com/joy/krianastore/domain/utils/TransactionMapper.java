package com.joy.krianastore.domain.utils;

import com.joy.krianastore.data.models.Transaction;
import com.joy.krianastore.domain.dto.TransactionDto;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class TransactionMapper {
    /**
     * Convert Transaction entity to DTO
     * @param transaction entity to be converted
     * @return resulting DTO
     */
    public static TransactionDto toDTO(Transaction transaction) {
        log.info("TransactionMapper toDTO for transaction: {}", transaction);
        return new TransactionDto(transaction.getCurrency(),transaction.getAmount(),transaction.isCredit());
    }

    /**
     * Convert TransactionDto to Transaction entity
     * @param dto the dto to be converted
     * @return the resulting entity
     */
    public static Transaction toEntity(TransactionDto dto) {
        log.info("TransactionMapper toEntity for transaction: {}", dto);
        Transaction transaction = new Transaction();
        transaction.setCurrency(dto.currency());
        transaction.setAmount(dto.amount());
        transaction.setTransactionDate(LocalDate.now());
        transaction.setCredit(dto.isCredit());
        return transaction;
    }
}
