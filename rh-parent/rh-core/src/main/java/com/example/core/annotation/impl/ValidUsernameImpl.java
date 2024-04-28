package com.example.core.annotation.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.example.core.annotation.ValidUsername;
import com.example.core.validation.Validate;

public class ValidUsernameImpl implements ConstraintValidator<ValidUsername, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Validate.isUsernameValid(value)) {
            return true;
        } else {
            return false;
        }
    }

}
