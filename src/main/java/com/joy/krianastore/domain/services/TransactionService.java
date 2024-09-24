package com.joy.krianastore.domain.services;

import com.joy.krianastore.core.CurrencyConversionClient;
import com.joy.krianastore.core.exception.ResourceNotFoundException;
import com.joy.krianastore.data.dao.StoreRepository;
import com.joy.krianastore.data.dao.TransactionRepository;
import com.joy.krianastore.data.models.Transaction;
import com.joy.krianastore.data.models.User;
import com.joy.krianastore.domain.dto.TransactionDto;
import com.joy.krianastore.domain.utils.TransactionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

/**
 * TransactionService handles business logic relation to transaction operations
 */
@Service
@AllArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final StoreRepository storeRepository;
    private final CurrencyConversionClient currencyConversionClient;

    /**
     * Records a new transaction in the database
     * @param transactionDTO is the transaction details
     * @param connectedUser is the currently logged-in user
     * @return the recorded transaction details
     * @throws ResourceNotFoundException if store of that user is not found
     */
    public TransactionDto recordTransaction(TransactionDto transactionDTO, Principal connectedUser) {
        log.info("Recording transaction {}", transactionDTO);
        Transaction transaction;
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        var optionalStore=storeRepository.findById(user.getStore().getId());
        if(optionalStore.isEmpty()){
            log.error("Store {} not found", user.getStore().getId());
            throw new ResourceNotFoundException("Store not found with id: " + user.getStore().getId());
        }
        var store=optionalStore.get();
        if (!"INR".equals(transactionDTO.currency())) {
            BigDecimal convertRate = currencyConversionClient.getConversionRate(transactionDTO.currency(), "INR");
            TransactionDto newDto=new TransactionDto("INR",transactionDTO.amount().multiply(convertRate), transactionDTO.isCredit());
            transaction= TransactionMapper.toEntity(newDto);
        }else{
            transaction=TransactionMapper.toEntity(transactionDTO);
        }
        transaction.setStore(store);
        transaction = transactionRepository.save(transaction);
        store.addTransaction(transaction);
        storeRepository.save(store);
        log.info("Transaction {} saved", transaction);
        return TransactionMapper.toDTO(transaction);
    }

    /**
     * Fetches transactions between specified dates
     * @param connectedUser currently logged-in user
     * @param startDate start date for the time period
     * @param endDate end date for the time period
     * @return returns list of transactions of the store of the user between the said dates
     */
    public List<TransactionDto> getTransactionsBetweenDates(Principal connectedUser, LocalDate startDate, LocalDate endDate) {
        log.info("Getting transactions between {} and {}", startDate, endDate);
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        log.info("Returning transactions between {} and {}", startDate, endDate);
        return transactionRepository.findAllByStoreIdAndTransactionDateBetween(user.getStore().getId(),startDate, endDate).stream().map(TransactionMapper::toDTO).toList();
    }

    public List<TransactionDto> getAllTransactions(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        log.info("Getting all transactions for user {}", user.getId());
        return transactionRepository.findAllByStoreId(user.getStore().getId()).stream().map(TransactionMapper::toDTO).toList();
    }
}
