package com.softlines.fastpos.validation.product;

import com.softlines.fastpos.dto.ProductDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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