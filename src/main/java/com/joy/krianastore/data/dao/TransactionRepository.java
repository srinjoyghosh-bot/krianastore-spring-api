package com.joy.krianastore.data.dao;

import com.joy.krianastore.data.models.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
/**
 * Repository interface for accessing and managing {@link Transaction} entities.
 * Extends the {@link MongoRepository} for standard CRUD operations.
 */
@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    /**
     * Fetches all transactions for a particular store for a given period
     * @param store_id the unique id for the store
     * @param startDate the beginning of the period
     * @param endDate the end of the period
     * @return the list of required transactions
     */
    List<Transaction> findAllByStoreIdAndTransactionDateBetween(String store_id, LocalDate startDate, LocalDate endDate);
    /**
     * Fetches all transactions for a particular store
     * @param store_id the unique id for the store
     * @return the list of required transactions
     */
    List<Transaction> findAllByStoreId(String store_id);
}
