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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    DtoService dtoService;

    ExceptionManagement exceptionManagement=new ExceptionManagement();

    @PostMapping("/save")
    public ResponseEntity addCategory(@RequestBody CategoryDto categoryDto) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findById(categoryDto.getId());

            if (!optionalCategory.isPresent()) {
                Category createdCategory = categoryRepository.save(dtoService.categoryDtoToCategory(categoryDto,false));

                return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.toCategoryDto(createdCategory));
            } else {
                return ResponseEntity.notFound().build();

            }
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        try {

            List<Category> categories = categoryRepository.findAll();
            if (categories == null || categories.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

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
            Optional<Category> optionalCategory = categoryRepository.findById(id);

            if (optionalCategory.isPresent())
                return ResponseEntity.ok().body(categoryMapper.toCategoryDto(optionalCategory.get()));
            else
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();


        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }

    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<List<CategoryDto>> getProductByName(@PathVariable String name) {
        try {

            List<Category> categories = categoryRepository.findByName(name);

            if (categories != null)
                return ResponseEntity.ok().body(categoryMapper.toCategoryDTOs(categories));
            else
                return ResponseEntity.notFound().build();

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<CategoryDto> editCategory(@PathVariable long id, @RequestBody CategoryDto categoryDto) {
        try {

            Optional<Category> optionalCategory = categoryRepository.findById(id);

            if (optionalCategory.isPresent()) {
                return ResponseEntity.ok().body(categoryMapper.toCategoryDto(categoryRepository.save(dtoService.categoryDtoToCategory(categoryDto,false))));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCategory(@PathVariable long id) {
        try {
            Category categoryToDel = categoryRepository.findById(id).get();
            if (categoryToDel != null) {

                categoryRepository.delete(categoryToDel);
                return ResponseEntity.ok().build();

            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }
}