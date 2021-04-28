package com.softlines.fastpos.validation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

public class PhoneNumberCollectionImpl implements ConstraintValidator<PhoneNumberCollection, Collection<String>> {
    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    @Override
    public boolean isValid(Collection<String> value, ConstraintValidatorContext context) {
        return value.stream().allMatch(s -> {
            Phonenumber.PhoneNumber phone = null;
            try {
                phone = phoneNumberUtil.parse(s,
                        Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
                return phoneNumberUtil.isValidNumber(phone);
            } catch (NumberParseException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

}
