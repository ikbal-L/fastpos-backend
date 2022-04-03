package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.DailyEarningsReport;
import com.softlines.fastpos.dto.DailyEarningsReportDto;
import com.softlines.fastpos.dto.filters.Filter;
import com.softlines.fastpos.dto.filters.Page;
import com.softlines.fastpos.dto.mapping.DailyExpenseReportMapper;
import com.softlines.fastpos.dto.service.filtering.DailyEarningsReportsFilterService;
import com.softlines.fastpos.repository.DailyExpenseReportRepository;
import com.softlines.fastpos.service.DailyExpenseReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/daily-earnings-report", produces = "application/json; charset=UTF-8")

public class DailyExpenseReportController {

    DailyExpenseReportService dailyExpenseReportService;

    DailyExpenseReportRepository dailyExpenseReportRepository;

    DailyExpenseReportMapper dailyExpenseReportMapper;

    final  DailyEarningsReportsFilterService dailyEarningsReportsFilterService;

    public DailyExpenseReportController(DailyExpenseReportService dailyExpenseReportService, DailyExpenseReportRepository dailyExpenseReportRepository, DailyExpenseReportMapper dailyExpenseReportMapper, DailyEarningsReportsFilterService dailyEarningsReportsFilterService) {
        this.dailyExpenseReportService = dailyExpenseReportService;
        this.dailyExpenseReportRepository = dailyExpenseReportRepository;
        this.dailyExpenseReportMapper = dailyExpenseReportMapper;
        this.dailyEarningsReportsFilterService = dailyEarningsReportsFilterService;
    }

    @PostMapping("/save")
    public ResponseEntity<DailyEarningsReportDto> createReport(/*@RequestBody DailyExpenseReportInputDataDto inputDataDto*/) {

        var generated = dailyExpenseReportService.generateDailyExpenseReport(false,null);
        var createdReportDto = dailyExpenseReportMapper.toDailyExpenseReportDto(generated);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReportDto);
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<DailyEarningsReportDto> updateReport(@PathVariable long id /* ,@RequestBody DailyExpenseReportInputDataDto inputDataDto*/ ) {

        var result = dailyExpenseReportRepository.findById(id);

        if (result.isEmpty()) return ResponseEntity.noContent().build();

        var updateDailyExpenseReport = dailyExpenseReportService.updateDailyExpenseReport(result.get());
        var createdReportDto = dailyExpenseReportMapper.toDailyExpenseReportDto(updateDailyExpenseReport);

        return ResponseEntity.ok().body(createdReportDto);
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<DailyEarningsReport> getReport(@PathVariable Long id) {
        var report = dailyExpenseReportRepository.findById(id);
        return report.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/get/{issuedDate}")
    public ResponseEntity<DailyEarningsReport> getReportByIssuedDate(@PathVariable Date issuedDate) {
        var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        var dateString = simpleDateFormat.format(issuedDate);
        var report = dailyExpenseReportRepository.findByIssuedDate(dateString);
        return report.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/get/date/today")
    public ResponseEntity<DailyEarningsReportDto> getReportOfTheDay() {

        DailyEarningsReport result;
        var today = LocalDate.now();
        var report = dailyExpenseReportRepository.findByIssuedDate(today);

        if (report.isEmpty()){
            result = dailyExpenseReportService.generateDailyExpenseReport(false,null);
            var dto = dailyExpenseReportMapper.toDailyExpenseReportDto(result);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        }else {

            if (!dailyExpenseReportService.isReportUpToDate(report.get(),today)){

                result = dailyExpenseReportService.updateDailyExpenseReport(report.get());
            }else {
                result = report.get();
            }

        }

        var dto = dailyExpenseReportMapper.toDailyExpenseReportDto(result);
        return ResponseEntity.ok(dto);

    }

    @GetMapping("/getall")
    public ResponseEntity<List<DailyEarningsReportDto>> getAllReports() {

        dailyExpenseReportService.updateReportsInRange();

        var reports = dailyExpenseReportRepository.findAll();

        if (!reports.isEmpty()) {

            return ResponseEntity.ok(dailyExpenseReportMapper.toDailyExpenseReportDtos(reports));
        } else {

            return ResponseEntity.noContent().build();
        }
    }


    @PostMapping("/getallbycriteria")
    public ResponseEntity<Page<DailyEarningsReportDto>> getReportsByPage(@RequestBody Filter filter) throws ParseException {

        dailyExpenseReportService.updateReportsInRange();

        var page = dailyEarningsReportsFilterService.buildQuery(filter);

        if (!page.isEmpty()) {

            return ResponseEntity.ok(page.mapToPage(dailyExpenseReportMapper::toDailyExpenseReportDtos));
        } else {

            return ResponseEntity.noContent().build();
        }
    }

}
