package com.softlines.fastpos.security.controllers;

import com.softlines.fastpos.domain.Printer;
import com.softlines.fastpos.security.securitydomain.securitydto.PrintingByCategoryConfigurationDto;
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

    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    PrintingByCategoryConfigurationMapper printingByCategoryConfigurationMapper;
    @Autowired
    PrintingByCategoryConfigurationRepository printingByCategoryConfigurationRepository;

    @GetMapping("/printers/getall")
    public ResponseEntity<List<Printer>> getPrinters(){
        var printers=  entityManager.createQuery("SELECT p FROM Printer  p ",Printer.class).getResultList();
//        if (printers.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(printers);
    }

    @GetMapping("/printing-by-category/getall")
    ResponseEntity<List<PrintingByCategoryConfigurationDto>>getAllCreateCategoryPrintingConfig(){
        var result = printingByCategoryConfigurationRepository.findAll();
        var dtos = printingByCategoryConfigurationMapper.toDTOs(result);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/printing-by-category/create")
    public ResponseEntity<PrintingByCategoryConfigurationDto> createCategoryPrintingConfig(PrintingByCategoryConfigurationDto dto){
        var entity = printingByCategoryConfigurationMapper.toEntity(dto);
        entity = printingByCategoryConfigurationRepository.save(entity);
        printingByCategoryConfigurationMapper.toExistingDto(entity,dto);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/printing-by-category/update/{id}")
    public ResponseEntity<PrintingByCategoryConfigurationDto> updateCategoryPrintingConfig(PrintingByCategoryConfigurationDto dto, @PathVariable Long id){
        if (!printingByCategoryConfigurationRepository.existsById(dto.getId())) return ResponseEntity.noContent().build();
        var entity = printingByCategoryConfigurationMapper.toEntity(dto);
        entity = printingByCategoryConfigurationRepository.save(entity);
        printingByCategoryConfigurationMapper.toExistingDto(entity,dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/printing-by-category/delete/{id}")
    public ResponseEntity<Void> deleteCategoryPrintingConfig(PrintingByCategoryConfigurationDto dto,@PathVariable Long id){
        if (!printingByCategoryConfigurationRepository.existsById(dto.getId())) return ResponseEntity.noContent().build();
        printingByCategoryConfigurationRepository.deleteById(dto.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
