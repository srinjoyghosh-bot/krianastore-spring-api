package com.joy.krianastore.data.dao;

import com.joy.krianastore.data.models.Store;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends MongoRepository<Store,String> {
}
