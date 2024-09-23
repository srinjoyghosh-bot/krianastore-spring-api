package com.joy.krianastore.domain.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ReportDto(BigDecimal totalCredit, BigDecimal totalDebit, BigDecimal netFlow,
                        Map<String, BigDecimal> dailyBreakdown) {
}
