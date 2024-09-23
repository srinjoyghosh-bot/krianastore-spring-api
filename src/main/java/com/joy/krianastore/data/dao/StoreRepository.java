package com.joy.krianastore.data.dao;

import com.joy.krianastore.data.models.Store;
import com.joy.krianastore.data.models.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository interface for accessing and managing {@link Store} entities.
 * Extends the {@link MongoRepository} for standard CRUD operations.
 */
@Repository
public interface StoreRepository extends MongoRepository<Store,String> {
}
