package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.dto.CategoryDto;
import com.softlines.fastpos.dto.mapping.CategoryMapper;
import com.softlines.fastpos.dto.service.DtoService;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    DtoService dtoService;

    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @PostMapping("/save")
    public ResponseEntity addCategory(@RequestBody CategoryDto categoryDto) {
        try {

            Category optionalCategory = categoryRepository.findByIdCategoryWithProducts(categoryDto.getId());

            if (optionalCategory ==null) {
                if (categoryDto.getName() != null && !categoryDto.getName().isEmpty()) {
                    Category createdCategory = categoryRepository.save(dtoService.categoryDtoToCategory(categoryDto, false));
                    return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.toCategoryDto(createdCategory));
                } else {
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

                }
            } else {

                return ResponseEntity.status(HttpStatus.FOUND).build();

            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<CategoryDto>> getCategories() {

        try {

            List<Category> categories = categoryRepository.findAllCategoriesWithProducts();

            if (categories == null || categories.isEmpty()) {
                return ResponseEntity.noContent().build();

            } else {
                return ResponseEntity.ok().body(categoryMapper.toCategoryDTOs(categories));
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }


    @GetMapping("/getmany")
    public ResponseEntity<List<CategoryDto>> getMany(@RequestBody List<Long> ids) {
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
    public ResponseEntity<CategoryDto> getCategory(@PathVariable long id) {

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
    public ResponseEntity<CategoryDto> editCategory(@PathVariable long id, @RequestBody CategoryDto categoryDto) {
        try {

           Category optionalCategory = categoryRepository.findByIdCategoryWithProducts(id);

            if (optionalCategory != null && id != 0 && categoryDto.getName() != null) {
                var category = dtoService.categoryDtoToCategory(categoryDto, false);
                var updatedCategory = categoryRepository.save(category);
                var updatedCategoryDto = categoryMapper.toCategoryDto(updatedCategory);
                return ResponseEntity.ok().body(updatedCategoryDto);
            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCategory(@PathVariable long id) {

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