package com.softlines.fastpos.controller;


import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.dto.ProductDto;
import com.softlines.fastpos.dto.mapping.ProductMapper;
import com.softlines.fastpos.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductRepository productRepository;
    private  ProductMapper productMapper;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @PostMapping(value = "/save", consumes = "application/json")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        try {
            Product existingProduct = productRepository.findById(product.getId()).get();
            if (existingProduct != null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(existingProduct);
            }
            Product createdProduct = productRepository.save(product);
            if (createdProduct == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<ProductDto>> getProducts() {
        try {
            return ResponseEntity.ok().body(productMapper.INSTANCE.toProductDTOs( productRepository.findAll()));
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable long id) {
        try {
            return ResponseEntity.ok().body(productMapper.toDto(productRepository.findById(id).get()));
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @GetMapping("/getByname/{name}")
    public ResponseEntity<List<Product>> getProductByName(@PathVariable String name) {
        try {

            List<Product> products = productRepository.findByName(name);
            if (products == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(products);
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<Product> editProduct(@PathVariable long id, @RequestBody Product product) {
        try {
            Product existingProduct = productRepository.findById(id).get();
            Assert.notNull(existingProduct, "Product not found");

            if (existingProduct != null) {
                existingProduct.setName(product.getName());
                existingProduct.setDescription(product.getDescription());
                existingProduct.setPrice(product.getPrice());
                existingProduct.setAvailableStock(product.getAvailableStock());
                existingProduct.setBackgroundString(product.getBackgroundString());
                existingProduct.setCategorieId(product.getCategorieId());
                existingProduct.setMuchInDemand(product.isMuchInDemand());
                existingProduct.setPlatter(product.isPlatter());
                existingProduct.setColor(product.getColor());
                existingProduct.setRank(product.getRank());
                existingProduct.setUnit(product.getUnit());
                existingProduct.setType(product.getType());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(productRepository.save(existingProduct));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(product);
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable long id) {
        try {
            Product productToDel = productRepository.findById(id).get();
            if (productToDel != null) {
                productRepository.delete(productToDel);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(productToDel);

            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(productToDel);

            }
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }
}


