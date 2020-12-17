package com.softlines.fastpos.validation.category;

import com.softlines.fastpos.dto.CategoryDto;
import com.softlines.fastpos.dto.ProductDto;
import com.softlines.fastpos.validation.product.ProductValidationIfCategoryIdEqualNullRankMustEqualNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImplCategoryValidationIfRankEqualNullProductIdsMustEqualNull implements ConstraintValidator<CategoryValidationIfRank_EqualNull_Than_ProductIds_MustEqualNull, CategoryDto> {


    public boolean isValid(CategoryDto categoryDto, ConstraintValidatorContext context) {
        boolean validityRankWithProductsIds = true;
        if (categoryDto.getRank() == null &&
                (categoryDto.getProductIds() == null || categoryDto.getProductIds().size() == 0)) {
            validityRankWithProductsIds = false;
        }

        return validityRankWithProductsIds;
    }

}