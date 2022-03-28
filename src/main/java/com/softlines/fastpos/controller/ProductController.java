package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.dto.ProductDto;
import com.softlines.fastpos.dto.mapping.ProductMapper;
import com.softlines.fastpos.dto.service.DtoServiceImpl;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
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
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/product", produces = "application/json; charset=UTF-8")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    private final DtoServiceImpl dtoService;

    private final ProductMapper productMapper;


    ExceptionManagement exceptionManagement = new ExceptionManagement();
    @PersistenceContext
    EntityManager entityManager;

    @PostMapping(value = "/save")
    public ResponseEntity<Long> addProduct(@Valid @RequestBody ProductDto productDto) {

        if (productDto.getId() == 0) {

            Product product = dtoService.productDtoToProduct(productDto, false);
            var created = productRepository.save(product);

            return ResponseEntity.status(HttpStatus.CREATED).body(created.getId());

        } else {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }

    }

    @PostMapping(value = "/savemany", consumes = "application/json")
    public ResponseEntity<List<Long>> addManyProduct(@Valid @RequestBody List<ProductDto> productDtoList) {

        List<Long> Ids = productDtoList.parallelStream().map(ProductDto::getId).collect(Collectors.toList());

        List<Product> products = dtoService.productDtoListToProductList(productDtoList, false);
        List<Product> savedProductList = productRepository.saveAll(products);
        List<Long> savedIds = savedProductList.parallelStream().map(Product::getId).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedIds);

    }


    //    @PreAuthorize("@apiAuth.checkGrants(authentication, 'Read_Product')")
    @GetMapping("/getall")
    public ResponseEntity<List<ProductDto>> getProducts() {
        try {
            List<Product> products = productRepository.findAllProductsWithAdditives();

            if (hasDuplicateRanks(products)) {
                products = productRepository.findAllProductsWithAdditives();
            }


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


    private boolean hasDuplicateRanks(List<Product> products) {
        var productsWithDupRank =
                products.stream()
                        .filter(c -> c.getRank() != null && c.getCategory() != null)
                        .collect(Collectors.groupingBy(Product::getCategory)).entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, productsListEntry -> productsListEntry.getValue().stream()
                                .collect(Collectors.groupingBy(Product::getRank))
                        )).entrySet().stream()
                        //filter products in the same category with the same rank
                        .filter(productMapEntry -> productMapEntry.getValue().entrySet().stream().anyMatch(rankListEntry -> rankListEntry.getValue().size() > 1))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (productsWithDupRank.isEmpty()) {
            return false;
        }

        List<Product> modifiedProductsWithDuplicateRanks = new ArrayList<>();
        for (var categoryGrouping : productsWithDupRank.entrySet()) {
            var rankGroupings = categoryGrouping.getValue().entrySet();
            for (var rankGrouping : rankGroupings) {

                var productsOfCategoryOfRank = rankGrouping.getValue();
                if (products.size() > 1) {
                    //keep only one product with the duplicate rank and "free" the rest
                    productsOfCategoryOfRank.stream().skip(1).forEach(product -> {
                        product.setRank(null);
                        product.setCategory(null);
                    });
                    modifiedProductsWithDuplicateRanks.addAll(productsOfCategoryOfRank);
                }

            }
        }

        productRepository.saveAll(modifiedProductsWithDuplicateRanks);
        return true;
    }


    @DeleteMapping("/delete/{id}")
    @Transactional(transactionManager = "transactionManager")
    public ResponseEntity deleteProduct(@Valid @PathVariable long id) {

        if (!productRepository.existsById(id)){
            return  ResponseEntity.notFound().build();
        }
        var query =entityManager.createNativeQuery("update orderitem set product_id = NULL  where product_id = :id").setParameter("id",id).executeUpdate();
        var query2 =entityManager.createNativeQuery("update products_additives set product_id = NULL  where product_id = :id").setParameter("id",id).executeUpdate();
        var query3 = entityManager.createNativeQuery("delete  from  product where id = :id").setParameter("id",id);


        return ResponseEntity.ok().build();
    }





    @PutMapping("/permutate")
    @Transactional(transactionManager = "transactionManager")
    public ResponseEntity<List<Long>> permutateProducts(@Valid @RequestBody List<ProductDto> productDtoList) {
        List<Long> ids = productDtoList.parallelStream().map(ProductDto::getId).collect(Collectors.toList());
        var  listedIdsCount = productRepository.findAllById(ids).size();


        if (listedIdsCount == productDtoList.size()) {
            List<Product> products = dtoService.productDtoListToProductList(productDtoList, false);
            var productX =products.get(0);
            var productY =products.get(1);
            var productXRank = productX.getRank();
            productX.setRank(null);
            productRepository.saveAndFlush(productX);
            productRepository.saveAndFlush(productY);
            productX.setRank(productXRank);
            productRepository.saveAndFlush(productX);



            return ResponseEntity.status(HttpStatus.OK).build();

        } else {
            return ResponseEntity.noContent().build();
        }

    }



}


