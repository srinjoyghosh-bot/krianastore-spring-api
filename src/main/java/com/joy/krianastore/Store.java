package com.joy.krianastore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.List;

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

    void addUser(User user) {
        if(users == null)
        {
            users = new ArrayList<User>();
        }
        users.add(user);
        user.setStore(this);
    }

    void addTransaction(Transaction txn) {
        if(transactions == null)
        {
            transactions = new ArrayList<Transaction>();
        }
        transactions.add(txn);
        txn.setStore(this);
    }
}
