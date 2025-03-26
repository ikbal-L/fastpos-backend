package com.softlines.fastpos.validation;

import com.softlines.fastpos.constants.MessageKeyConstants;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PhoneNumberImpl.class})
public @ interface PhoneNumber {
    String message() default MessageKeyConstants.CUSTOMER_MOBILE_VALIDATION_ERROR;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
