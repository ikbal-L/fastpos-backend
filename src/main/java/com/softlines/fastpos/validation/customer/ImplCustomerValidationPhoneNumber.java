package com.softlines.fastpos.validation.customer;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.softlines.fastpos.validation.customer.CustomerValidationPhoneNumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImplCustomerValidationPhoneNumber implements ConstraintValidator<CustomerValidationPhoneNumber, String> {

    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {

        try {
            Phonenumber.PhoneNumber phone = phoneNumberUtil.parse(phoneNumber,
                    Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
            return phoneNumberUtil.isValidNumber(phone);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return false;

    }

}