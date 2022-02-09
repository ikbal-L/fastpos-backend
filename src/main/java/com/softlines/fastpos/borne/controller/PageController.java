package com.softlines.fastpos.borne.controller;

import com.softlines.fastpos.borne.PageItemType;
import com.softlines.fastpos.borne.dto.PageItem;
import com.softlines.fastpos.borne.dto.PageModel;
import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.dto.CategoryDto;
import com.softlines.fastpos.dto.ProductDto;
import com.softlines.fastpos.dto.mapping.AdditiveMapper;
import com.softlines.fastpos.dto.mapping.CategoryMapper;
import com.softlines.fastpos.dto.mapping.ProductMapper;
import com.softlines.fastpos.dto.service.DtoService;
import com.softlines.fastpos.dto.service.DtoServiceImpl;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.AdditiveRepository;
import com.softlines.fastpos.repository.CategoryRepository;
import com.softlines.fastpos.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/page", produces = "application/json; charset=UTF-8")
@RequiredArgsConstructor
public class PageController {

    ExceptionManagement exceptionManagement = new ExceptionManagement();

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final DtoService dtoService;

    private final AdditiveMapper additiveMapper;

    private final AdditiveRepository additiveRepository;

    private final EntityManagerFactory entityManagerFactory;

    private final ProductMapper productMapper;

    private final ProductRepository productRepository;

    List<PageModel> pageModels = new ArrayList<>();

    @GetMapping("/getall")
    public ResponseEntity<List<PageModel>> getCategories() {

        try {
            pageModels.clear();

            List<Category> categoryList = categoryRepository.findAllCategoriesWithProducts();
            List<PageItem> pageItems = new ArrayList<>();

            categoryList.forEach(category -> pageItems.add(PageItem.builder().
                    image(category.getImageUrl()).title(category.getName()).id(category.getId())
                    .uniqueChoice(true).pageItemType(PageItemType.Category).build()));

            pageModels.add(PageModel.builder().title("Home").subtitle("Menu").maxGlobal(1).pageName("0").family("menu")
                    .listItems(pageItems).nbrColonnes(3).nbrUnitMax(1).build());

            List<PageItem> pageItemListLivraison = new ArrayList<>();
            pageItemListLivraison.add(PageItem.builder().title("SUR PLACE").uniqueChoice(true)
                    .pageItemType(PageItemType.Livraison).build());

            pageItemListLivraison.add(PageItem.builder().title("A EMPORTER").uniqueChoice(true)
                    .pageItemType(PageItemType.Livraison).build());

            PageModel pageModelLivraison = PageModel.builder().pageName("liv").family("livraison")
                    .title("Livraison").subtitle("livraison").nbrColonnes(2).maxGlobal(1)
                    .nbrMinObligatoir(1).listItems(pageItemListLivraison).build();

            categoryList.forEach(category ->
            {
                List<PageItem> pageItemsProducts = new ArrayList<>();

                for (Product product : category.getProducts()) {
                    pageItemsProducts.add(PageItem.builder().title(product.getName())
                            .image(product.getImageUrl()).id(product.getId()).prix1(product.getPrice())
                            .uniqueChoice(true).pageItemType(PageItemType.Product).build());
                }

                pageModels.add(PageModel.builder().title(category.getName())
                        .subtitle(category.getName().toLowerCase()).pageName("Category " + category.getId())
                        .family(category.getId() + "").listItems(pageItemsProducts).maxGlobal(1)
                        .nbrColonnes(3).nbrUnitMax(1).build());

                List<Long> idsList = category.getProducts().parallelStream().map(Product::getId)
                        .collect(Collectors.toList());

                List<Product> products = productRepository.findManyProductsWithAdditives(idsList);


                pageModels.add(pageModelLivraison);

                for (Product product : products) {

                    List<PageItem> pageItemsAdditives = new ArrayList<>();

                    for (Additive additive : product.getAdditives()) {
                        pageItemsAdditives.add(PageItem.builder().title(additive.getDescription())
                                .uniqueChoice(false).id(additive.getId()).image(additive.getImageUrl())
                                .pageItemType(PageItemType.Additive).nextPage(pageModelLivraison).build());
                    }

                    pageModels.add(PageModel.builder().title("الإضافات")
                            .subtitle(product.getName().toLowerCase()).pageName("Product " + product.getId())
                            .family(product.getId() + "").listItems(pageItemsAdditives).maxGlobal(30)
                            .nbrColonnes(3).nbrUnitMax(2).build());
                }

            });


            pageModels.forEach(pageModel -> {
                for (PageItem pageItem : pageModel.getListItems()) {
                    for (PageModel model : pageModels) {
                        if (pageItem.getNextPage() == null &&
                                (pageItem.getPageItemType() + " " + pageItem.getId()).equals(model.getPageName())) {

                            if (pageItem.getPageItemType() == PageItemType.Product && model.getListItems().isEmpty()) {
                                pageItem.setNextPage(pageModelLivraison);
                            } else {
                                pageItem.setNextPage(model);
                            }
                        }
                    }
                }
            });


            return ResponseEntity.ok().body(pageModels);

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }


}
