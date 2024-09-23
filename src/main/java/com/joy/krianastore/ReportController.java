package com.joy.krianastore;

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
    @GetMapping
    public ResponseEntity<ApiResponse<ReportDto>> getReports(@RequestParam(required = false) String period, Principal connectedUser) {
        ReportDto response;
        if(period==null || period.equals("weekly")){
            response=reportsService.generateWeeklyReport(connectedUser);
        } else if (period.equals("monthly")) {
            response=reportsService.generateMonthlyReport(connectedUser);
        }else if (period.equals("yearly")) {
            response=reportsService.generateYearlyReport(connectedUser);
        }else {
            //TODO throw exception
            return null;
        }
        return new ResponseEntity<>(new ApiResponse<>(true,"Reports fetched!",response), HttpStatus.OK);
    }
}
