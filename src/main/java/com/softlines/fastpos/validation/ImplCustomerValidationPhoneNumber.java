package com.softlines.fastpos.validation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.softlines.fastpos.dto.CustomerDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImplCustomerValidationPhoneNumber implements ConstraintValidator<CustomerValidationPhoneNumber, CustomerDto> {

    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    public boolean isValid(CustomerDto customerDto, ConstraintValidatorContext context) {
        try {
            Phonenumber.PhoneNumber phone = phoneNumberUtil.parse(customerDto.getMobile(),
                    Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
            return phoneNumberUtil.isValidNumber(phone);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return false;

    }

}