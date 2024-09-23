package com.joy.krianastore;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionDto(@NotNull @NotEmpty String currency,
                             @NotNull @NotEmpty @Pattern(regexp = "^\\d+(\\.\\d+)?$", message = "Must be a valid non-negative number without any alphabets or symbols") BigDecimal amount,
                             @NotNull boolean isCredit) {
}
