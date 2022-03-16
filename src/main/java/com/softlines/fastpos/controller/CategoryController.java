package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.dto.CategoryDto;
import com.softlines.fastpos.dto.ProductDto;
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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    @PostMapping("/save")
    public ResponseEntity<Long> addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        if (categoryDto.getId() == 0) {
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


            if (_validateCategoryRanks(categories)) {
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

    private boolean _validateCategoryRanks(List<Category> categories) {
        var categoriesWithDupRank =
                categories.stream()
                        .filter(c -> c.getRank() != null)
                        .collect(Collectors.groupingBy(Category::getRank))
                        .entrySet().stream()
                        .filter(e -> e.getValue().size() > 1)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        List<List<Category>> _dupes = new ArrayList<>();
        List<List<Product>> _productsOfDupes = new ArrayList<>();
        for (var entry : categoriesWithDupRank.entrySet()) {

            var maxNumberOfProducts = entry
                    .getValue()
                    .stream()
                    .mapToInt(category -> category.getProducts().size())
                    .max()
                    .orElse(1);
            var _categoriesWithLeastNumberOfProducts = entry
                    .getValue()
                    .stream()
                    .filter(category -> category.getProducts().size() < maxNumberOfProducts)
                    .collect(Collectors.toList());
            _categoriesWithLeastNumberOfProducts.forEach(category -> {
                category.getProducts().forEach(product -> product.setRank(null));
                _productsOfDupes.add(category.getProducts());
                category.setRank(null);
                category.setProducts(null);
            });
            _dupes.add(_categoriesWithLeastNumberOfProducts);
        }
        var _dupCategories = _dupes
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        var _inactiveProducts = _productsOfDupes
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        productRepository.saveAll(_inactiveProducts);
        categoryRepository.saveAll(_dupCategories);
        return _dupCategories.size() > 0;
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
    public ResponseEntity deleteCategory(@Valid @PathVariable long id) {

        try {

            Category categoryToDel = categoryRepository.findByIdCategoryWithProducts(id);
            if (categoryToDel != null) {

                categoryRepository.delete(categoryToDel);
                return ResponseEntity.ok().build();

            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }
}