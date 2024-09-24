package com.joy.krianastore.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
/**
 * Represents a financial transaction within the system.
 * This entity is persisted in the database and can be a debit or credit.
 */
@Document(collection = "transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    private String id;
    private String currency;
    private BigDecimal amount;
    private LocalDate transactionDate;
    private boolean isCredit;

    @ToString.Exclude
    @DBRef
    private Store store;
}
