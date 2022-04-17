package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.dto.CategoryDto;
import com.softlines.fastpos.dto.mapping.CategoryMapper;
import com.softlines.fastpos.dto.service.DtoService;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.CategoryRepository;
import com.softlines.fastpos.repository.ProductRepository;
import com.softlines.fastpos.security.exceptions.RankDuplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController

@RequestMapping(value = "/api/category", produces = "application/json; charset=UTF-8")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    DtoService dtoService;

    ExceptionManagement exceptionManagement = new ExceptionManagement();
    @PersistenceContext
    EntityManager entityManager;

    @PostMapping("/save")
    public ResponseEntity<Long> addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        if (categoryDto.getId() == null) {
            Category category = dtoService.categoryDtoToCategory(categoryDto, false);
            Category createdCategory = categoryRepository.save(category);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory.getId());

        } else {

            return ResponseEntity.status(HttpStatus.FOUND).build();

        }
    }

    @PostMapping("/savemany")
    @Transactional(transactionManager = "transactionManager")
    public ResponseEntity<List<CategoryDto>> addManyCategory(@Valid @RequestBody List<CategoryDto> categoryDtos) throws RankDuplicationException {

        List<Category> categories = dtoService.categoriesDtoToCategories(categoryDtos, false);
        var activeCategoriesCount =
                categories.stream().filter(c -> c.getRank() != null).count();
        var categoriesWithDupRank =
                categories.stream().filter(c -> c.getRank() != null).collect(Collectors.groupingBy(Category::getRank));
        if (activeCategoriesCount > categoriesWithDupRank.size()) {
            throw new RankDuplicationException("Duplicate ranks ");
        }
        List<Category> createdCategories = categoryRepository.saveAll(categories);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.toCategoryDTOs(createdCategories));
    }

    @GetMapping("/getall")
    public ResponseEntity<List<CategoryDto>> getCategories() {

        try {

            List<Category> categories = categoryRepository.findAllCategoriesWithProducts();


            if (HasDuplicateRanks(categories)) {
                categories = categoryRepository.findAllCategoriesWithProducts();
            }

            if (categories == null || categories.isEmpty()) {
                return ResponseEntity.noContent().build();

            } else {
                return ResponseEntity.ok().body(categoryMapper.toCategoryDTOs(categories));
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    private boolean HasDuplicateRanks(List<Category> categories) {
        var categoriesWithDupRank =
                categories.stream()
                        .filter(c -> c.getRank() != null)
                        .collect(Collectors.groupingBy(Category::getRank))
                        .entrySet().stream()
                        .filter(e -> e.getValue().size() > 1)//each rank has more that one category
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (categoriesWithDupRank.size() ==0){
            return  false;
        }
        List<Category> modifiedCategoriesWithDuplicateRanks = new ArrayList<>();

        AtomicInteger maxRank = new AtomicInteger(categories.stream().filter(c->c.getRank()!= null).mapToInt(Category::getRank).max().orElse(1));
        for (var entry : categoriesWithDupRank.entrySet()) {

            entry.getValue().stream().skip(1).forEach(category -> {
                var newRank = maxRank.incrementAndGet();
                category.setRank(newRank);
            });
            modifiedCategoriesWithDuplicateRanks.addAll(entry.getValue());
        }

        categoryRepository.saveAll(modifiedCategoriesWithDuplicateRanks);
        return true;
    }


    @GetMapping("/getmany")
    public ResponseEntity<List<CategoryDto>> getMany(@Valid @RequestBody List<Long> ids) {

        try {

            List<Category> categories = categoryRepository.findManyCategoriesWithProducts(ids);

            if (categories == null || categories.isEmpty()) {
                return ResponseEntity.noContent().build();

            } else {
                return ResponseEntity.ok().body(categoryMapper.toCategoryDTOs(categories));
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }


    @GetMapping("/get/{id}")
    public ResponseEntity<CategoryDto> getCategory(@Valid @PathVariable long id) {

        try {
            Category optionalCategory = categoryRepository.findByIdCategoryWithProducts(id);

            if (optionalCategory != null && id != 0)
                return ResponseEntity.ok().body(categoryMapper.toCategoryDto(optionalCategory));
            else
                return ResponseEntity.noContent().build();


        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }


    @PutMapping("/put/{id}")
    public ResponseEntity<CategoryDto> editCategory(@PathVariable long id, @Valid @RequestBody CategoryDto categoryDto) {

        var optionalCategory = categoryRepository.findByIdWithPrintingConfiguration(id);

        if (optionalCategory.isPresent() && id != 0 && categoryDto.getName() != null) {
            var category = dtoService.categoryDtoToCategory(categoryDto, false);
            var printingConfiguration = optionalCategory.get().getPrintingByCategoryConfiguration();
            if (printingConfiguration != null) {
                var categoryToReplace = printingConfiguration.getCategories().stream().filter(category1 -> category1.getId() == id).findFirst();
                if (categoryToReplace.isPresent()) {
                    printingConfiguration.getCategories().remove(categoryToReplace.get());
                    printingConfiguration.getCategories().add(category);
                }
                category.setPrintingByCategoryConfiguration(printingConfiguration);
            }
            var updatedCategory = categoryRepository.save(category);
            var updatedCategoryDto = categoryMapper.toCategoryDto(updatedCategory);
            return ResponseEntity.ok().body(updatedCategoryDto);
        } else {
            return ResponseEntity.noContent().build();
        }

    }

    @PutMapping("/putmany")
    public ResponseEntity<List<Long>> editManyCategories(@Valid @RequestBody List<CategoryDto> categoryDtos) throws RankDuplicationException {
        List<Long> ids = categoryDtos.parallelStream().map(CategoryDto::getId).collect(Collectors.toList());
        List<Category> products = categoryRepository.findAllById(ids);

        if (products.size() == categoryDtos.size()) {


            List<Category> categoryList = dtoService.categoriesDtoToCategories(categoryDtos, false);

            var activeCategoriesCount =
                    categoryList.stream().filter(c -> c.getRank() != null).count();
            var categoriesWithDupRank =
                    categoryList.stream().filter(c -> c.getRank() != null).collect(Collectors.groupingBy(Category::getRank));
            if (activeCategoriesCount > categoriesWithDupRank.size()) {
                throw new RankDuplicationException("Duplicate ranks ");
            }


            List<Category> updatedCategoryList = categoryRepository.saveAll(categoryList);

            return ResponseEntity.status(HttpStatus.OK).build();

        } else {
            return ResponseEntity.noContent().build();
        }

    }


    @DeleteMapping("/delete/{id}")
    @Transactional(transactionManager = "transactionManager")
    public ResponseEntity deleteCategory(@Valid @PathVariable long id) {

        if (!categoryRepository.existsById(id)){
            return  ResponseEntity.noContent().build();
        }
        var query =entityManager.createNativeQuery("update product set category_id = NULL , rank = null  where category_id = :id").setParameter("id",id).executeUpdate();
//        TODO decouple GroupingByCategory from category
//        TODO update daily earning report service to reflect changes related to category

        var query3 = entityManager.createNativeQuery("delete  from  category where id = :id").setParameter("id",id).executeUpdate();


        return ResponseEntity.ok().build();
    }

    @PutMapping("/permutate")
    @Transactional(transactionManager = "transactionManager")
    public ResponseEntity<List<Long>> permutateCategories(@Valid @RequestBody List<CategoryDto> categoryDtos) {
        List<Long> ids = categoryDtos.parallelStream().map(CategoryDto::getId).collect(Collectors.toList());
        var listedIdsCount = categoryRepository.findAllById(ids).size();


        if (listedIdsCount == categoryDtos.size()) {
            var categoryList = dtoService.categoriesDtoToCategories(categoryDtos, false);
            var categoryX =categoryList.get(0);
            var categoryY =categoryList.get(1);
            var categoryXRank = categoryX.getRank();
            categoryX.setRankNullOnPermutation();
            categoryRepository.saveAndFlush(categoryX);
            categoryRepository.saveAndFlush(categoryY);
            categoryX.setRank(categoryXRank);
            categoryRepository.saveAndFlush(categoryX);


            return ResponseEntity.status(HttpStatus.OK).build();

        } else {
            return ResponseEntity.noContent().build();
        }

    }
}