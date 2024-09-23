package com.joy.krianastore.data.dao;

import com.joy.krianastore.data.models.Transaction;
import com.joy.krianastore.data.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Repository interface for accessing and managing {@link User} entities.
 * Extends the {@link MongoRepository} for standard CRUD operations.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    /**
     * Finding user by user email
     * @param email the email for which we need to find the user
     * @return the user details
     */
    Optional<User> findByEmail(String email);
}
