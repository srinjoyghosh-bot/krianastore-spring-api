package com.joy.krianastore.domain.dto;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Data Transfer Object for transferring reports data between the client and server.
 * @param totalCredit total credited amount in the period
 * @param totalDebit total debited amount in the period
 * @param netFlow net cash flow in the period
 * @param dailyBreakdown daily net cash flow in the period
 */
public record ReportDto(BigDecimal totalCredit, BigDecimal totalDebit, BigDecimal netFlow,
                        Map<String, BigDecimal> dailyBreakdown) {
}
