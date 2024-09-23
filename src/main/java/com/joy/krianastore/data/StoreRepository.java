package com.joy.krianastore.data;

import com.joy.krianastore.domain.models.Store;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends MongoRepository<Store,String> {
}
