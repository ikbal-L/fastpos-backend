package com.softlines.fastpos.validation.person;

import com.softlines.fastpos.constants.MessageKeyConstants;
import com.softlines.fastpos.validation.customer.ImplCustomerValidationPhoneNumber;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ImplPersonValidationPhoneNumber.class})
public @interface PersonValidationPhoneNumber {

    String message() default MessageKeyConstants.PERSON_MOBILE_VALIDATION_ERROR;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}