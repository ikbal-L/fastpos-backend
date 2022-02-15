package com.softlines.fastpos.security.controllers;

import com.softlines.fastpos.domain.Printer;
import com.softlines.fastpos.dto.PrintingByCategoryConfigurationDto;
import com.softlines.fastpos.repository.PrinterRepository;
import com.softlines.fastpos.security.securitydomain.securitydto.PrinterDto;
import com.softlines.fastpos.security.securitydomain.securitymapper.PrinterMapper;
import com.softlines.fastpos.security.securitydomain.securitymapper.PrintingByCategoryConfigurationMapper;
import com.softlines.fastpos.repository.PrintingByCategoryConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RestController
@RequestMapping("/config/app-settings")
public class AppSettingsController {


    @Autowired
    PrintingByCategoryConfigurationMapper printingByCategoryConfigurationMapper;
    @Autowired
    PrintingByCategoryConfigurationRepository printingByCategoryConfigurationRepository;
    @Autowired
    PrinterMapper printerMapper;
    @Autowired
    PrinterRepository printerRepository;

    @GetMapping("/printers/getall")
    public ResponseEntity<List<PrinterDto>> getPrinters(){
        var printers=  printerRepository.findAll();
//        if (printers.isEmpty()) return ResponseEntity.noContent().build();
       var dtos = printerMapper.toDTOs(printers);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/printers/savemany")
    public ResponseEntity<List<PrinterDto>> createPrinters(@RequestBody  List<PrinterDto> dtos){
        var entities = printerMapper.toEntities(dtos);
        entities = printerRepository.saveAll(entities);
        printerMapper.toExistingDtos(entities,dtos);
        return ResponseEntity.ok(dtos);
    }



    @GetMapping("/printing-by-category/getall")
    ResponseEntity<List<PrintingByCategoryConfigurationDto>>getAllCreateCategoryPrintingConfig(){
        var result = printingByCategoryConfigurationRepository.findAll();
        var dtos = printingByCategoryConfigurationMapper.toDTOs(result);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/printing-by-category/save")
    public ResponseEntity<PrintingByCategoryConfigurationDto> createCategoryPrintingConfig(@RequestBody  PrintingByCategoryConfigurationDto dto){
        var entity = printingByCategoryConfigurationMapper.toEntity(dto);
        com.softlines.fastpos.domain.PrintingByCategoryConfiguration finalEntity = entity;
        entity.getCategories().forEach(c->c.setPrintingByCategoryConfiguration(finalEntity));
        entity = printingByCategoryConfigurationRepository.save(entity);
        printingByCategoryConfigurationMapper.toExistingDto(entity,dto);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/printing-by-category/update/{id}")
    public ResponseEntity<PrintingByCategoryConfigurationDto> updateCategoryPrintingConfig(@RequestBody PrintingByCategoryConfigurationDto dto, @PathVariable Long id){
        if (!printingByCategoryConfigurationRepository.existsById(dto.getId())) return ResponseEntity.noContent().build();
        var entity = printingByCategoryConfigurationMapper.toEntity(dto);
        entity = printingByCategoryConfigurationRepository.save(entity);
        printingByCategoryConfigurationMapper.toExistingDto(entity,dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/printing-by-category/delete/{id}")
    public ResponseEntity<Void> deleteCategoryPrintingConfig(@RequestBody PrintingByCategoryConfigurationDto dto,@PathVariable Long id){
        if (!printingByCategoryConfigurationRepository.existsById(dto.getId())) return ResponseEntity.noContent().build();
        printingByCategoryConfigurationRepository.deleteById(dto.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
