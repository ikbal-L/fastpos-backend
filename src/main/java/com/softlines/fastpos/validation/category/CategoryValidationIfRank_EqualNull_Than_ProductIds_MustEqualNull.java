package com.softlines.fastpos.validation.category;

import com.softlines.fastpos.constants.MessageKeyConstants;
import com.softlines.fastpos.validation.product.ImplProductValidationIfCategoryIdEqualNullRankMustEqualNull;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ImplCategoryValidationIfRankEqualNullProductIdsMustEqualNull.class})
public @interface CategoryValidationIfRank_EqualNull_Than_ProductIds_MustEqualNull {

    String message() default MessageKeyConstants.CATEGORY_IF_RANK_EQUAL_NULL_PRODUCTS_IDS_MUST_EQUAL_NULL_VALIDATION_ERROR;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}