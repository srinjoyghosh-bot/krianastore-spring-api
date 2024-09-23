package com.joy.krianastore.domain.models;

/**
 * ADMIN can create new user, read and create transactions for its store.
 * READ_WRITE can create and read transactions for its store.
 * READ_ONLY can only read transactions of its store.
 */
public enum Role {
    ADMIN,
    READ_WRITE,
    READ_ONLY
}
