package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/save")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        try {
            Category existingCategory = categoryRepository.findById(category.getId()).get();
            if (existingCategory != null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(existingCategory);
            }
            Category createdCategory = categoryRepository.save(category);
            if (existingCategory != null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(category);
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);

            }
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Category>> getCategories() {
        try {


            if (categoryRepository.findAll() == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(categoryRepository.findAll());

            } else {
                return ResponseEntity.ok().body(categoryRepository.findAll());

            }
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Optional<Category>> getCategory(@PathVariable long id) {
        try {
            if (categoryRepository.findById(id) == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(categoryRepository.findById(id));

            } else {
                return ResponseEntity.ok().body(categoryRepository.findById(id));
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @PutMapping("/put/{id}")
    public void editCategory(@PathVariable long id, @RequestBody Category category) {
        try {
            Category existingCategory = categoryRepository.findById(id).get();
            Assert.notNull(existingCategory, "Category not found");
            existingCategory.setBackgroundString(existingCategory.getBackgroundString());
            existingCategory.setRank(existingCategory.getRank());
            existingCategory.setName(existingCategory.getName());
            categoryRepository.save(existingCategory);
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCategory(@PathVariable long id) {
        try {
            Category categoryToDel = categoryRepository.findById(id).get();
            categoryRepository.delete(categoryToDel);
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }
}