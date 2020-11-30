package com.softlines.fastpos.validation.product;

import com.softlines.fastpos.constants.MessageKeyConstants;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ImplProductValidationIfCategoryIdEqualNullRankMustEqualNull.class})
public @interface ProductValidationIfCategoryIdEqualNullRankMustEqualNull {

    String message() default MessageKeyConstants.PRODUCT_IF_CATEGORY_ID_EQUAL_NULL_RANK_MUST_EQUAL_NULL_VALIDATION_ERROR;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}