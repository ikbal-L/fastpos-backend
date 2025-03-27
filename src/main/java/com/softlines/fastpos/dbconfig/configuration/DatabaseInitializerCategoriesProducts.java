package com.softlines.fastpos.dbconfig.configuration;

import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.repository.CategoryRepository;
import com.softlines.fastpos.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Configuration
@Slf4j
public class DatabaseInitializerCategoriesProducts {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final Random random = new Random();

    public DatabaseInitializerCategoriesProducts(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Bean
    @Order(2) // Ensure this initializer runs after another one with lower order
    public ApplicationRunner initCategoriesAndProducts() {
        return args -> {
            if (categoryRepository.count() == 0 && productRepository.count() == 0) {
                initCategoriesAndProductsData();
            } else {
                log.info("Categories and products already exist. Skipping initialization.");
            }
        };
    }

    @Transactional
    public void initCategoriesAndProductsData() {
        String[][] categoryData = {
                {"Pizza", "Margherita", "Pepperoni", "BBQ Chicken", "Hawaiian", "Veggie"},
                {"Burgers", "Cheeseburger", "Bacon Burger", "Mushroom Swiss", "BBQ Burger", "Veggie Burger"},
                {"Salads", "Caesar Salad", "Greek Salad", "Garden Salad", "Cobb Salad", "Tuna Salad"},
                {"Desserts", "Chocolate Cake", "Cheesecake", "Apple Pie", "Brownie", "Ice Cream"},
                {"Drinks", "Cola", "Lemonade", "Iced Tea", "Orange Juice", "Milkshake"}
        };
        int categoryRank = 1;
        for (String[] categoryInfo : categoryData) {
            String categoryName = categoryInfo[0];
            int finalCategoryRank = categoryRank;
            Category category = categoryRepository.findByName(categoryName).orElseGet(() -> {
                Category newCategory = new Category();
                newCategory.setName(categoryName);
                newCategory.setBackgroundString(generateRandomMaterialColor());
                newCategory.setRank(finalCategoryRank);
                return categoryRepository.save(newCategory);
            });
            int productRank = 1;
            for (int i = 1; i < categoryInfo.length; i++) {
                String productName = categoryInfo[i];
                if (!productRepository.existsByName(productName)) {
                    Product product = new Product();
                    product.setName(productName);
                    product.setPrice(10.0 + (i * 2)); // Example pricing logic
                    product.setCategory(category);
                    product.setBackgroundString(generateRandomMaterialColor());
                    product.setRank(productRank);
                    productRank++;
                    productRepository.save(product);
                }
            }
            categoryRank++;
        }
    }

    private String generateRandomMaterialColor() {
        String[] materialColors = {
                "#F44336", "#E57373", "#EF5350", "#E53935", "#D32F2F", "#C62828", "#AD1457", "#D81B60",
                "#9C27B0", "#BA68C8", "#AB47BC", "#8E24AA", "#7B1FA2", "#6A1B9A", "#4A148C", "#3F51B5",
                "#7986CB", "#5C6BC0", "#3949AB", "#303F9F", "#283593", "#1A237E", "#1565C0", "#42A5F5",
                "#64B5F6", "#1E88E5", "#1976D2", "#1565C0", "#0D47A1", "#00838F", "#0097A7", "#00ACC1",
                "#26C6DA", "#4DB6AC", "#26A69A", "#00897B", "#00796B", "#00695C", "#2E7D32", "#388E3C",
                "#43A047", "#4CAF50", "#66BB6A", "#81C784", "#9CCC65", "#D4E157", "#FDD835", "#FFEB3B",
                "#FFCA28", "#FFA000", "#FB8C00", "#F57C00", "#EF6C00", "#E65100", "#6D4C41", "#8D6E63",
                "#A1887F", "#BDBDBD", "#757575", "#616161", "#424242", "#212121"
        };
        return materialColors[random.nextInt(materialColors.length)];
    }
}
