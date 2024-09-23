package com.joy.krianastore.data;

import com.joy.krianastore.domain.models.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findAllByStoreId(String storeId);

    List<Transaction> findAllByStoreIdAndTransactionDateBetween(String store_id, LocalDate startDate, LocalDate endDate);
}
