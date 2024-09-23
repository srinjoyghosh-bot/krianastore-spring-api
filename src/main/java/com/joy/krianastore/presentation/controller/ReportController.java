package com.joy.krianastore.presentation.controller;

import com.joy.krianastore.core.exception.RateLimitExceededException;
import com.joy.krianastore.domain.services.RateLimitingService;
import com.joy.krianastore.domain.dto.ApiResponse;
import com.joy.krianastore.domain.dto.ReportDto;
import com.joy.krianastore.domain.services.ReportsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/reports")
@AllArgsConstructor
public class ReportController {
    private final ReportsService reportsService;
    private final RateLimitingService rateLimitingService;

    @GetMapping
    public ResponseEntity<ApiResponse<ReportDto>> getReports(@RequestParam(required = false) String period, Principal connectedUser) {
        if (rateLimitingService.allowRequest("/api/reports")) {
            throw new RateLimitExceededException("You have exceeded the allowed number of requests. Please try again later.");
        }
        ReportDto response;
        if (period == null || period.equals("weekly")) {
            response = reportsService.generateWeeklyReport(connectedUser);
        } else if (period.equals("monthly")) {
            response = reportsService.generateMonthlyReport(connectedUser);
        } else if (period.equals("yearly")) {
            response = reportsService.generateYearlyReport(connectedUser);
        } else {
            throw new IllegalArgumentException("Invalid period: " + period);
        }
        return new ResponseEntity<>(new ApiResponse<>(true, "Reports fetched!", response), HttpStatus.OK);
    }
}
