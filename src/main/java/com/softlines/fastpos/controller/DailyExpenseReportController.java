package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.DailyExpenseReport;
import com.softlines.fastpos.dto.DailyExpenseReportInputDataDto;
import com.softlines.fastpos.repository.DailyExpenseReportRepository;
import com.softlines.fastpos.service.DailyExpenseReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(value = "/api/dailyexpensereport", produces = "application/json")
public class DailyExpenseReportController {
    @Autowired
    DailyExpenseReportService dailyExpenseReportService;

    @Autowired
    DailyExpenseReportRepository dailyExpenseReportRepository;

    @PostMapping("/save")
    public ResponseEntity<DailyExpenseReport>createReport(@RequestBody DailyExpenseReportInputDataDto inputDataDto) {

        var date = new Date();
        var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        var dateString = simpleDateFormat.format(date);
        var report = dailyExpenseReportRepository.findByIssuedDate(dateString);

        if (true) {
            var generated = dailyExpenseReportService.generateDailyExpenseReport(inputDataDto);
            var createdReport = dailyExpenseReportRepository.save(generated);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReport);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DailyExpenseReport> getReport(@PathVariable Long id) {
        var report = dailyExpenseReportRepository.findById(id);
        if (report.isPresent()){
            return ResponseEntity.ok(report.get());
        }else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/get/issued-date/{date}")
    public ResponseEntity<DailyExpenseReport> getReportByIssuedDate(@PathVariable Date issuedDate) {
        var date = new Date();
        var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        var dateString = simpleDateFormat.format(date);
        var report = dailyExpenseReportRepository.findByIssuedDate(dateString);
        if (report.isPresent()){
            return ResponseEntity.ok(report.get());
        }else {
            return ResponseEntity.noContent().build();
        }
    }

}
