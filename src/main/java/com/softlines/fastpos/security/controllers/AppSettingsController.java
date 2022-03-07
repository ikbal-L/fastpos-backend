package com.softlines.fastpos.security.controllers;

import com.softlines.fastpos.domain.Printer;
import com.softlines.fastpos.domain.PrintingByCategoryConfiguration;
import com.softlines.fastpos.dto.PrintingByCategoryConfigurationDto;
import com.softlines.fastpos.repository.CategoryRepository;
import com.softlines.fastpos.repository.PrinterRepository;
import com.softlines.fastpos.security.securitydomain.securitydto.PrinterDto;
import com.softlines.fastpos.security.securitydomain.securitymapper.PrinterMapper;
import com.softlines.fastpos.security.securitydomain.securitymapper.PrintingByCategoryConfigurationMapper;
import com.softlines.fastpos.repository.PrintingByCategoryConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Autowired
    CategoryRepository categoryRepository;

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
    public ResponseEntity<Long> createCategoryPrintingConfig(@RequestBody  PrintingByCategoryConfigurationDto dto){
        var entity = printingByCategoryConfigurationMapper.toEntity(dto,categoryRepository);
        HandlePrintingConfigurationCategoryAssociation(dto, entity);

        entity = printingByCategoryConfigurationRepository.save(entity);
        categoryRepository.saveAll(entity.getCategories());
        printingByCategoryConfigurationMapper.toExistingDto(entity,dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity.getId());
    }

    @PutMapping("/printing-by-category/put/{id}")
    public ResponseEntity<PrintingByCategoryConfigurationDto> updateCategoryPrintingConfig(@RequestBody PrintingByCategoryConfigurationDto dto, @PathVariable Long id){
        if (!printingByCategoryConfigurationRepository.existsById(dto.getId())) return ResponseEntity.noContent().build();
        HandleRemovedCategories(dto);
        var entity = printingByCategoryConfigurationMapper.toEntity(dto,categoryRepository);
        HandlePrintingConfigurationCategoryAssociation(dto, entity);
        entity = printingByCategoryConfigurationRepository.save(entity);
        printingByCategoryConfigurationMapper.toExistingDto(entity,dto);
        return ResponseEntity.ok(dto);
    }

    private void HandlePrintingConfigurationCategoryAssociation( PrintingByCategoryConfigurationDto dto, PrintingByCategoryConfiguration entity) {
        var cats = categoryRepository.findAllById(dto.getCategoryIds());
        entity.setCategories(Set.copyOf(cats));
        PrintingByCategoryConfiguration finalEntity = entity;
        entity.getCategories().forEach(c -> c.setPrintingByCategoryConfiguration(finalEntity));
    }

    private void HandleRemovedCategories(PrintingByCategoryConfigurationDto dto) {
        var previousConfigState = printingByCategoryConfigurationRepository.findById(dto.getId()).get();
        var removedCats = previousConfigState.getCategories()
                .stream().filter(c-> dto.getCategoryIds().stream().noneMatch(cId-> cId.equals(c.getId()))).collect(Collectors.toList());
        removedCats.forEach(c->c.setPrintingByCategoryConfiguration(null));
        categoryRepository.saveAll(removedCats);
    }

    @DeleteMapping("/printing-by-category/delete/{id}")
    public ResponseEntity<?> deleteCategoryPrintingConfig(@PathVariable Long id){
        if (!printingByCategoryConfigurationRepository.existsById(id )) return ResponseEntity.noContent().build();
        var categories = categoryRepository.findAllByPrintingByCategoryConfigurationId(id);
        categories.forEach(category -> category.setPrintingByCategoryConfiguration(null));
        categoryRepository.saveAll(categories);
        categoryRepository.flush();
        printingByCategoryConfigurationRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
