package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.dto.ProductDto;
import com.softlines.fastpos.dto.mapping.ProductMapper;
import com.softlines.fastpos.dto.service.DtoServiceImpl;
import com.softlines.fastpos.repository.ProductRepository;
import com.softlines.fastpos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DtoServiceImpl dtoService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductService productService;

    @PostMapping(value = "/save", consumes = "application/json")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) {

        try {
            Optional<Product> optionalProduct = productRepository.findById(productDto.getId());

            if (!optionalProduct.isPresent()) {
                Product product = dtoService.productDtoToProduct(productDto,false);
                return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toProductDto(productRepository.save(product)));
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }

    }

    @GetMapping("/getall")
    public ResponseEntity<List<ProductDto>> getProducts() {

        try {
            List<Product> products = productRepository.findAll();
            //products.forEach(p -> p.getAdditives());
//            List<Product> products = productService.findAll();
            if (products != null)
                return ResponseEntity.ok().body(productMapper.toProductDTOs(products));
            else
                return ResponseEntity.notFound().build();

        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable long id) {

        try {
            Optional<Product> optionalProduct = productRepository.findById(id);
            if (optionalProduct.isPresent())
                return ResponseEntity.ok().body(productMapper.toProductDto(optionalProduct.get()));
            else
                return ResponseEntity.notFound().build();

        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }

    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<List<ProductDto>> getProductByName(@PathVariable String name) {
        try {

            List<Product> products = productRepository.findByName(name);

            if (products != null)
                return ResponseEntity.ok(productMapper.toProductDTOs(products));
            else
                return ResponseEntity.notFound().build();

        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<ProductDto> editProduct(@PathVariable long id, @RequestBody ProductDto productDto) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(id);

            if (optionalProduct.isPresent()) {

                Product product = dtoService.productDtoToProduct(productDto,false);
                return ResponseEntity.status(HttpStatus.OK).body(productMapper.toProductDto(productRepository.save(product)));

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(productDto);
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProduct(@PathVariable long id) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(id);
            if (optionalProduct.isPresent()) {

                productRepository.delete(optionalProduct.get());
                return ResponseEntity.ok().build();

            } else {

                return ResponseEntity.notFound().build();

            }
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }
}


