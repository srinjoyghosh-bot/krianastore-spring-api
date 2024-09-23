package com.joy.krianastore.data.models.enums;

/**
 * Enum representing the type of user
 * ADMIN can create new user, read and create transactions for its store.
 * READ_WRITE can create and read transactions for its store.
 * READ_ONLY can only read transactions of its store.
 */
public enum Role {
    ADMIN,
    READ_WRITE,
    READ_ONLY
}
