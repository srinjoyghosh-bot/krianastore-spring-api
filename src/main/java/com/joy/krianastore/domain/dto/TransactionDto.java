package com.joy.krianastore.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

/**
 * Data Transfer Object for transferring transaction data between the client and server.
 * @param currency currency in which transaction is recorded
 * @param amount amount of transaction
 * @param isCredit indicator if transaction is Money-In or Money-Out
 */
public record TransactionDto(@NotNull @NotEmpty String currency,
                             @NotNull @NotEmpty @Pattern(regexp = "^\\d+(\\.\\d+)?$", message = "Must be a valid non-negative number without any alphabets or symbols") BigDecimal amount,
                             @NotNull boolean isCredit) {
}
