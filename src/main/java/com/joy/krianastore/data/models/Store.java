package com.joy.krianastore.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a store which has multiple users and transactions associated with it
 */
@Document(collection = "stores")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Store {
    @Id
    private String id;
    private String name;

    @DBRef
    private List<User> users;

    @DBRef
    private List<Transaction> transactions;

    public void addUser(User user) {
        if(users == null)
        {
            users = new ArrayList<User>();
        }
        users.add(user);
        user.setStore(this);
    }

    public void addTransaction(Transaction txn) {
        if(transactions == null)
        {
            transactions = new ArrayList<Transaction>();
        }
        transactions.add(txn);
        txn.setStore(this);
    }
}
