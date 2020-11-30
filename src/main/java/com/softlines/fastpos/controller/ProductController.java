package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.dto.ProductDto;
import com.softlines.fastpos.dto.mapping.ProductMapper;
import com.softlines.fastpos.dto.service.DtoServiceImpl;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.ProductRepository;
import com.softlines.fastpos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "localhost", maxAge = 3600)
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DtoServiceImpl dtoService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductService productService;

    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @PostMapping(value = "/save", consumes = "application/json")
    public ResponseEntity<Long> addProduct(@Valid @RequestBody ProductDto productDto) {

        try {
           Optional<Product> optionalProduct = productRepository.findById(productDto.getId());

            if (optionalProduct.isEmpty()) {

                    Product product = dtoService.productDtoToProduct(productDto, false);
                    var created = productRepository.save(product);
                    return ResponseEntity.status(HttpStatus.CREATED).body(created.getId());
//                    return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toProductDto(productRepository.save(product)));

            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }
    @PreAuthorize("hasAuthority('Read_Product')")
//    @PreAuthorize("hasRole('admin') or ha")
    @GetMapping("/getall")
    public ResponseEntity<List<ProductDto>> getProducts( ) {

        try {

            List<Product> products = productRepository.findAllProductsWithAdditives();
//            List<Product> products = productRepository.findAll();

            if (products == null || products.isEmpty() )
                return ResponseEntity.noContent().build();

            else {
//                var productDtos = productMapper.toProductDTOsWithAdditives(products);
                var productDtos = productMapper.toProductDTOs(products);
                return ResponseEntity.ok().body(productDtos);
            }
        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @GetMapping("/getmany")
    public ResponseEntity<List<ProductDto>> getMany(@RequestBody List<Long> ids) {

        try {

            List<Product> products = productRepository.findManyProductsWithAdditives(ids);

            if (ids!=null && products == null || products.isEmpty())
                return ResponseEntity.noContent().build();
            else
                return ResponseEntity.ok().body(productMapper.toProductDTOs(products));

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }


    @GetMapping("/get/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable long id) {

        try {

            Product optionalProduct = productRepository.findByIdProductWithAdditives(id);

            if (id != 0)
                return ResponseEntity.ok().body(productMapper.toProductDto(optionalProduct));
            else
                return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<List<ProductDto>> getProductByName(@PathVariable String name) {
        try {

            List<Product> products = productRepository.findByName(name);

            if (products != null)
                return ResponseEntity.ok(productMapper.toProductDTOs(products));
            else
                return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);

        }
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<ProductDto> editProduct(@PathVariable long id, @RequestBody ProductDto productDto) {
        try {
            Product optionalProduct = productRepository.findByIdProductWithAdditives(id);

            if ( id != 0 && productDto.getName() != null) {

                Product product = dtoService.productDtoToProduct(productDto, false);
                Product updatedProduct = productRepository.save(product);
                ProductDto updatedProductDto= productMapper.toProductDto(updatedProduct);
                return ResponseEntity.status(HttpStatus.OK).body(updatedProductDto);

            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProduct(@PathVariable long id) {

        try {
            Product optionalProduct = productRepository.findByIdProductWithAdditives(id);

            if (optionalProduct!=null) {
                productRepository.delete(optionalProduct);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }
}


