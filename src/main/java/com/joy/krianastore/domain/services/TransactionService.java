package com.joy.krianastore.domain.services;

import com.joy.krianastore.core.CurrencyConversionClient;
import com.joy.krianastore.data.StoreRepository;
import com.joy.krianastore.data.TransactionRepository;
import com.joy.krianastore.domain.models.Transaction;
import com.joy.krianastore.domain.models.User;
import com.joy.krianastore.presentation.dto.TransactionDto;
import com.joy.krianastore.utils.TransactionMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final StoreRepository storeRepository;
    private final CurrencyConversionClient currencyConversionClient;

    public TransactionDto recordTransaction(TransactionDto transactionDTO, Principal connectedUser) {
        Transaction transaction;
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        var optionalStore=storeRepository.findById(user.getStore().getId());
        if(optionalStore.isEmpty()){
            //TODO throw exception
            return null;
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
        return TransactionMapper.toDTO(transaction);
    }

    public List<Transaction> getTransactionsBetweenDates(Principal connectedUser, LocalDate startDate, LocalDate endDate) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return transactionRepository.findAllByStoreIdAndTransactionDateBetween(user.getStore().getId(),startDate, endDate);
    }
}
