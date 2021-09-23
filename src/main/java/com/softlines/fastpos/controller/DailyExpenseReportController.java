package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.DailyExpenseReport;
import com.softlines.fastpos.dto.DailyExpenseReportDto;
import com.softlines.fastpos.dto.DailyExpenseReportInputDataDto;
import com.softlines.fastpos.dto.mapping.DailyExpenseReportMapper;
import com.softlines.fastpos.repository.DailyExpenseReportRepository;
import com.softlines.fastpos.service.DailyExpenseReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/dailyexpensereport", produces = "application/json")
public class DailyExpenseReportController {

    DailyExpenseReportService dailyExpenseReportService;

    DailyExpenseReportRepository dailyExpenseReportRepository;

    DailyExpenseReportMapper dailyExpenseReportMapper;

    public DailyExpenseReportController(DailyExpenseReportService dailyExpenseReportService, DailyExpenseReportRepository dailyExpenseReportRepository, DailyExpenseReportMapper dailyExpenseReportMapper) {
        this.dailyExpenseReportService = dailyExpenseReportService;
        this.dailyExpenseReportRepository = dailyExpenseReportRepository;
        this.dailyExpenseReportMapper = dailyExpenseReportMapper;
    }

    @PostMapping("/save")
    public ResponseEntity<DailyExpenseReportDto> createReport(@RequestBody DailyExpenseReportInputDataDto inputDataDto) throws ParseException {


//        var report = dailyExpenseReportRepository.findByIssuedDate(dateString);

        var generated = dailyExpenseReportService.generateDailyExpenseReport(inputDataDto,false);
        var createdReport = dailyExpenseReportRepository.save(generated);
        var createdReportDto = dailyExpenseReportMapper.toDailyExpenseReportDto(createdReport);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReportDto);

//        if (report.isEmpty()) {
//            var generated = dailyExpenseReportService.generateDailyExpenseReport(inputDataDto);
//            var createdReport = dailyExpenseReportRepository.save(generated);
//            var createdReportDto = dailyExpenseReportMapper.toDailyExpenseReportDto(createdReport);
//            return ResponseEntity.status(HttpStatus.CREATED).body(createdReportDto);
//        } else {
//            return ResponseEntity.badRequest().build();
//        }
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<DailyExpenseReportDto> updateReport( @PathVariable long id,@RequestBody DailyExpenseReportInputDataDto inputDataDto ) throws ParseException {
        var report = dailyExpenseReportRepository.findById(id);
        if (report.isEmpty()) return ResponseEntity.noContent().build();
        var date = new Date();
        var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        var currentDateString = simpleDateFormat.format(date);
        var reportDateString = simpleDateFormat.format(report.get().getIssuedDate());
        if (!currentDateString.equals(reportDateString)) return ResponseEntity.badRequest().build();



        var generated = dailyExpenseReportService.updateDailyExpenseReport(report.get(),inputDataDto);
        var createdReport = dailyExpenseReportRepository.save(generated);
        var createdReportDto = dailyExpenseReportMapper.toDailyExpenseReportDto(createdReport);

        return ResponseEntity.ok().body(createdReportDto);
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<DailyExpenseReport> getReport(@PathVariable Long id) {
        var report = dailyExpenseReportRepository.findById(id);
        if (report.isPresent()) {
            return ResponseEntity.ok(report.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/get/{issuedDate}")
    public ResponseEntity<DailyExpenseReport> getReportByIssuedDate(@PathVariable Date issuedDate) {
        var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        var dateString = simpleDateFormat.format(issuedDate);
        var report = dailyExpenseReportRepository.findByIssuedDate(dateString);
        if (report.isPresent()) {
            return ResponseEntity.ok(report.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<DailyExpenseReportDto>> getAllReports() {
        var reports = dailyExpenseReportRepository.findAll();
        if (!reports.isEmpty()) {
            return ResponseEntity.ok(dailyExpenseReportMapper.toDailyExpenseReportDtos(reports));
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}
