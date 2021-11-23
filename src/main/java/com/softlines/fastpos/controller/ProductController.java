package com.softlines.fastpos.controller;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.dto.ProductDto;
import com.softlines.fastpos.dto.mapping.ProductMapper;
import com.softlines.fastpos.dto.service.DtoServiceImpl;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.ProductRepository;
import com.softlines.fastpos.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/product",produces = "application/json")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    private final DtoServiceImpl dtoService;

    private final ProductMapper productMapper;

    private final ProductService productService;

    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @PostMapping(value = "/save")
    public ResponseEntity<Long> addProduct(@Valid @RequestBody ProductDto productDto) {

        if (productDto.getId()==0) {

            Product product = dtoService.productDtoToProduct(productDto, false);
            var created = productRepository.save(product);

            return ResponseEntity.status(HttpStatus.CREATED).body(created.getId());

        } else {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }

    }

    @PostMapping(value = "/savemany", consumes = "application/json")
    public ResponseEntity<List<Long>> addManyProduct(@Valid @RequestBody List<ProductDto> productDtoList) {

        try {
            List<Long> Ids = productDtoList.parallelStream().map(ProductDto::getId).collect(Collectors.toList());
            List<Product> productList = productRepository.findAllById(Ids);

            if (productList.size() == 0) {

                List<Product> products = dtoService.productDtoListToProductList(productDtoList, false);
                List<Product> savedProductList = productRepository.saveAll(products);
                List<Long> savedIds = savedProductList.parallelStream().map(Product::getId).collect(Collectors.toList());

                return ResponseEntity.status(HttpStatus.CREATED).body(savedIds);

            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

        @PreAuthorize("@apiAuth.checkGrants(authentication, 'Read_Product')")
    @GetMapping("/getall")
    public ResponseEntity<List<ProductDto>> getProducts() {
        try {
            List<Product> products = productRepository.findAllProductsWithAdditives();

            if (products == null || products.isEmpty())
                return ResponseEntity.noContent().build();

            else {
                var productDtos = productMapper.toProductDTOs(products);
                return ResponseEntity.ok().body(productDtos);
            }
        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @GetMapping("/getmany")
    public ResponseEntity<List<ProductDto>> getMany(@Valid @RequestBody List<Long> ids) {

        try {

            List<Product> products = productRepository.findManyProductsWithAdditives(ids);

            if (ids != null && products == null || products.isEmpty())
                return ResponseEntity.noContent().build();
            else
                return ResponseEntity.ok().body(productMapper.toProductDTOs(products));

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }


    @GetMapping("/get/{id}")
    public ResponseEntity<ProductDto> getProduct(@Valid @PathVariable long id) {

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
    public ResponseEntity<List<ProductDto>> getProductByName(@Valid @PathVariable String name) {
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
    public ResponseEntity<ProductDto> editProduct(@Valid @PathVariable long id, @Valid @RequestBody ProductDto productDto) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(id);

            if (optionalProduct.isPresent()) {

                Product product = dtoService.productDtoToProduct(productDto, false);
                Product updatedProduct = productRepository.save(product);
                ProductDto updatedProductDto = productMapper.toProductDto(updatedProduct);
                return ResponseEntity.status(HttpStatus.OK).body(updatedProductDto);

            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @PutMapping("/putmany")
    public ResponseEntity<List<Long>> editManyProduct(@Valid @RequestBody List<ProductDto> productDtoList) {
        try {

            List<Long> ids = productDtoList.parallelStream().map(ProductDto::getId).collect(Collectors.toList());
            List<Product> products = productRepository.findAllById(ids);

            if (products.size() == productDtoList.size()) {

                List<Product> savedProductList = dtoService.productDtoListToProductList(productDtoList, false);
                List<Product> updatedProductList = productRepository.saveAll(savedProductList);

                return ResponseEntity.status(HttpStatus.OK).build();

            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProduct(@Valid @PathVariable long id) {

        try {
            Product optionalProduct = productRepository.findByIdProductWithAdditives(id);

            if (optionalProduct != null) {
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


