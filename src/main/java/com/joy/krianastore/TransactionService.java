package com.joy.krianastore;

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

    public TransactionDto recordTransaction(TransactionDto transactionDTO,Principal connectedUser) {
        Transaction transaction;
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        var optionalStore=storeRepository.findById(user.getStore().getId());
        if(optionalStore.isEmpty()){
            //TODO throw exception
            return null;
        }
        var store=optionalStore.get();
        if (!"INR".equals(transactionDTO.currency())) {
            BigDecimal convertedAmount = currencyConversionClient.convertCurrency(transactionDTO.currency(), "INR", transactionDTO.amount());
            TransactionDto newDto=new TransactionDto("INR",convertedAmount, transactionDTO.isCredit());
            transaction=TransactionMapper.toEntity(newDto);
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
