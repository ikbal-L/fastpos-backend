package com.softlines.fastpos.validation.product;

import com.softlines.fastpos.dto.ProductDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImplProductValidationIfCategoryIdEqualNullRankMustEqualNull implements ConstraintValidator<ProductValidationIfCategoryIdEqualNullRankMustEqualNull, ProductDto> {


    public boolean isValid(ProductDto productDto, ConstraintValidatorContext context) {
        boolean validityCategoryIdWithRank = false;
        if (productDto.getCategoryId() != null && productDto.getRank() != null) {
            validityCategoryIdWithRank = true;
        }
        if (productDto.getCategoryId() == null && productDto.getRank() == null) {
            validityCategoryIdWithRank = true;
        }

        return validityCategoryIdWithRank;
    }

}